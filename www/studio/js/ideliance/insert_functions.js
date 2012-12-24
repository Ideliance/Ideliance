/*
 * Copyright 2012 Azmal Mougamadou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

////////////////////////// Fonctions utilitaires du panel Insertion ////////////////////

//Met a jour le contenu du sujet de la relation inverse
function updateSujetInv(){
      $('#sujetInv').val($('#complement').val());
}

//Met a jour le contenu du complement de la relation inverse
function updateCompInv(){
      $('#complementInv').val($('#sujet').val());
}

//Met a jour le contenu du verbe de la relation inverse si on entre une lettre
function updateVerbInv(){
      if($('#relation').val() != "") {
            $('#relationInv').val("inverse_of_" + $('#relation').val());
      }
      else {
            $('#relationInv').val("");
      }
}

// Verifie que le champ en argument de la fonction a ete renseigne
function isInputMissing(inputName, inputValue, date){
      if (inputValue == '') {
            printErrorOn(inputName, date);
            return true;
      }
      return false;
}

//Affiche dans la console d'informations, un message d'erreur (champ manquant)
function printErrorOn(element, errorTime) {
      clearOutput();
      printMessage(errorTime, " - ERREUR : Le " + decode(element) + " est manquant !");
}

//Affiche dans la console la confirmation d'ajout de relation
function printAddRelationInfo(sujet, verbe, complement, date) {
      printMessage(date, " - INFO - Relation ajout\351e : "+decode(sujet)+" "+decode(verbe)+" "+decode(complement));
}

//Affiche dans la console un warning (la relation existe deja)
function printWarning(sujet, verbe, complement, date) {
      printMessage(date, " - WARN - Relation d\351j\340 existante : "+decode(sujet)+" "+decode(verbe)+" "+decode(complement)+ " [Ajout annul\351]");
}

// Ajoute une liaison entre une relation et son inverse
function linkOppositeRelations(ridEdge1, ridEdge2){

      execute('update '+ridEdge1+ ' set inv = ' + ridEdge2 );
      execute('update '+ridEdge2+ ' set inv = ' + ridEdge1 ); 
      
}

// Teste si le sujet existe en base et retourne son rid, retourne null sinon
function getSubjectRid(sujet){
      var querySujet = execute('select from index:subject where key=\''+ sujet +'\'');
      if (querySujet.result[0]) {
            return querySujet.result[0].rid;
      }
      return null;
}

// Retourne true si une relation existe de label "label" existe entre les noeuds de rid "sujetRid" et "compRid"
function isThereARelationBetween(sujetRid, compRid, label){
      var queryVerb = execute('select from (select flatten(rid) from index:verb where key=\''+ label +'\') where out = \''+ sujetRid +'\' and in = \''+ compRid +'\'');
      return (!!(queryVerb.result[0]));
}

//Ajoute une relation de label "label" entre les noeuds de rid "sujetRid" et "compRid", retourne le rid de la relation
function createEdgeBetween(sujetRid, compRid, label){
      if(sujetRid != compRid) {
            return (execute('create edge from ' + sujetRid + ' to ' + compRid + ' set label = \'' + label + '\', createdTime = sysdate()')).result[0]['@rid'];
      }
      else {
            return ($.parseJSON(execute('insert into ographedge (in,out,label, createdTime) values ('+sujetRid+','+compRid+',\''+label+'\', sysdate())')))['@rid'];
      }
}

//Ordonne la liste des resultats de l'autocompletion pour afficher ceux qui commencent par term en premier
function filterBeginningElemFirst(array, term) {
      var beginList=new Array();
      var otherList=new Array();
      for(var i =0, size = array.length ; i< size ; i++){
            if(array[i].label.toLowerCase().indexOf(term)==0) {beginList.push(array[i]);}
            else {otherList.push(array[i]);}
      }
      beginList.sort(function(a,b){return a.label.toLowerCase() > b.label.toLowerCase()});
      otherList.sort(function(a,b){return a.label.toLowerCase() > b.label.toLowerCase()});
      return beginList.concat(otherList);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


// Verifie que les inputs sont non vides
function areInputsCorrect(sujet, verbe, complement, date){
      if (isInputMissing("sujet", sujet, date)) {return false;}
      if(isInputMissing("verbe", verbe, date)) {return false;}
      if(isInputMissing("complement", complement, date)) {return false;}
      
      return true;
}

// On verifie si sujet et complement n'existent pas deja en base. On teste l'existence de la relation
// La methode query renvoie un objet ayant un champ result, contenant le tableau des donnees resultats
function doRelationsAlreadyExist(sujetRid, compRid, sujet, verbe, complement, verbeInv, date){

      var relationExists = false;
      var relationInvExists = false;
      
      if(sujetRid && compRid){
            relationExists = isThereARelationBetween(sujetRid, compRid, verbe);
            relationInvExists = isThereARelationBetween(compRid, sujetRid, verbeInv);
      }
      /////////////////////////////////////////////////////////////////////
      
      if(relationExists){
            printWarning(sujet, verbe, complement, date);
            return true;
      }
      if(relationInvExists){
            printWarning(complement, verbeInv, sujet, date);
            return true;
      }
      
      return false;
}

// Tente d'ajouter la relation saisie (utilisee dans panelInsertRelation)
function addRelation(){
      
      //MAJ des champs symetriques
      updateSujetInv();
      updateCompInv();
      if($("#relationInv").val().indexOf('inverse_of_') > -1){
            updateVerbInv();
      }
      
      var date = new Date();
      
      if(!canUserWrite()) { 
            alert("Vous n'avez pas les droits suffisants.");
            return;
      }
      
      // Recuperation des inputs
      var sujet = encode($("#sujet").val());
      var verbe = encode($("#relation").val());
      var complement = encode($("#complement").val());
      var verbeInv = encode($("#relationInv").val());
      
      try {
            if(!areInputsCorrect(sujet, verbe, complement, date)) {return ;}
            
            clearOutput();
            
            var sujetRid = getSubjectRid(sujet);
            var compRid = getSubjectRid(complement);
            
            if(doRelationsAlreadyExist(sujetRid, compRid, sujet, verbe, complement, verbeInv, date)) {return;}
            
      
            // Ajout du premier noeud et creation de l'objet jQuery a partir du JSON, si non existant
            if(!sujetRid) {
                  var sujetNode = $.parseJSON(execute('create vertex set name = \'' +  sujet + '\''));
                  sujetRid = sujetNode['@rid'];
            }
            
            // Ajout du second noeud si non existant et si different du noeud de depart
            if(!compRid) {
                  if(sujet != complement) {
                        var compNode = $.parseJSON(execute('create vertex set name = \'' +  complement + '\''));
                        compRid = compNode['@rid'];
                  }
                  else compRid = sujetRid;
            }

            // Creation des deux relations et liaison entre les relations inverses
            var ridRel1 = createEdgeBetween(sujetRid, compRid, verbe);
            var ridRel2 = createEdgeBetween(compRid, sujetRid, verbeInv);
            linkOppositeRelations(ridRel1, ridRel2);
            
            // Message d'erreur si un des ajouts a echoue
            if(ridRel1 == null || ridRel2 == null) {
                  alert("Une erreur est survenue");
                  return ;
            }
            
            // Message de succes si tout a fonctionne correctement
            printAddRelationInfo(sujet, verbe, complement, date);
            printAddRelationInfo(complement, verbeInv, sujet, date); 
      
            $("#sujet, #relation, #complement, #sujetInv, #relationInv, #complementInv").val("");
            $('#sujet').focus();
            
      }
      catch(e) {
            alert(e.message);
            return false;
      }
}