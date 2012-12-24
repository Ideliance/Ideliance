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

////////////////////////// Fonctions utilitaires des panels de modifications relations/ entites ////////////////////

// Renvoie une copie de la liste des lignes selectionnees
function getSelectedRows(){
      /*var selectedArrow=$("#queryResultTable").jqGrid('getGridParam','selarrrow');
      return selectedArrow.slice(0,selectedArrow.length);*/
      
      return selArr.slice(0,selArr.length);
      
}

// Supprime dans le tableau les lignes d'identifiant ceux donnes en argument
function deleteSelectedRowsInGrid(rowList){
      for(i in rowList){
                  $("#queryResultTable").jqGrid('delRowData',rowList[i]);
            }
}

// Supprime en base les relations grace a leurs ids donnes en argument
function deleteEdgesFromList(edgesList){
      for(var i in edgesList){
                  execute('delete edge '+edgesList[i]);
            }
}

// Teste si une ligne au moins a ete selectionnee
function noRowsSelected(selectedRowsList, elementType){
      if (!selectedRowsList || selectedRowsList.length <=0) {
            alert("Aucune "+elementType+" n'a \351t\351 s\351lectionn\351e !");
            return true;
      }
      return false;
}

// Ajoute les aretes au edges du noeud en argument
function addEdgeLinksToNode(edges, nodeRid, direction){
      if(edges){
            for (var i = 0, size = edges.length ; i < size; i++){
                  execute('update ' + nodeRid + ' add '+direction+' = '+ edges[i]['@rid']);
            }
      }
}

// Demande le nom du nouvel element fusionne
function askNewMergedName(elementType, mergedElements){
      return prompt('Vous allez fusionner les '+elementType+'s :\n'+ mergedElements.join(',\n')+'\n\nEntrez le nom de la nouvelle '+elementType+' :', mergedElements[0]);
}


////////////////////////////////////////////////////////////////////////////////////////////////////////

// Affiche les donnees et retourne leur longueur
function processResponse(data) {
      displayResultSet(data["result"]);
      return (data["result"]).length ;
}


// On recupere les relations (sujet, verbe, complement), ou les enti. Les autres colonnes sont cachees
function loadData(type) {

      if(type != 'entities' && type != 'relations') {
            alert('Le type dans loadData est incorrect');
            return;
      }
      
      var typeIsEntity = type == 'entities';
      var query = '';
      
      query = typeIsEntity ? "select @rid, name as Sujet from OGraphVertex" : "select @rid, inv.@rid as invRid, out.name as Sujet, label as Verbe, in.name as Complement, createdTime as Date from OGraphEdge order by createdTime desc";
      
      orientServer.setEvalResponse(false);
      var commandResult = execute(query);
      orientServer.setEvalResponse(true);

      if (commandResult && commandResult.charAt(0) == '{') {
            commandResponse = orientServer.transformResponse(commandResult);
      } else {
            commandResponse = commandResult;
      }

      if (commandResult == null) {
            $("#output").text(orientServer.getErrorMessage());
      } 
      else {
            var length = processResponse(commandResponse);
            clearOutput();
            printMessage(new Date(), " - Retrieved "+ length +" "+type+".");
      }
      
      //on cache les colonnes non pertinentes et on resize 
      var width = $("#queryResultTable").jqGrid('getGridParam', 'width'); // get current width
      var hidedColumns;
      if(typeIsEntity){
            hidedColumns= ["rid","@rid"];
      }
      else{
            hidedColumns = ["rid", "invRid","@rid"];
      }
      $("#queryResultTable").jqGrid('hideCol', hidedColumns);
      $("#queryResultTable").jqGrid('setGridWidth', width);
      var sortCol = typeIsEntity ? 'Sujet' : 'Date';
      var sortOrder = typeIsEntity ? 'asc' : 'desc';
      
      //On ajoute des handlers afin de gerer les selections sur plusieurs pages (non gere de base)
      // cf utils.js
      $("#queryResultTable").jqGrid('setGridParam',{
             onSelectRow: rowSelectionHandler,
             onSelectAll: selectAllHandler,
             gridComplete: gridLoadInit,
             sortname: sortCol,
             sortorder : sortOrder
      });
      $("#queryResultTable").trigger('reloadGrid')
}


// Renomme une relation ou un sujet, elle est utilise dans les panels de modifications d'entites et relations
function renameProcess(type){
      
      if(!canUserWrite()) { 
            alert("Vous n'avez pas les droits suffisants.");
            return;
      }
      
      var typeIsSubject = type == "subject";
      var typeIsVerb = type == "verb";
      
      
      if(!typeIsSubject && !typeIsVerb){
            alert("The type in renameProcess is incorrect");
            return;
      }
      
      var typeName = typeIsSubject ? 'entit\351' : 'relation';
      
      var selectedRow = selArr[0];
      
      if(noRowsSelected(selectedRow, typeName)) {return;}
      
      try{
            var currentRow = commandResponse.result[selectedRow - 1];
            var oldName = typeIsSubject ? currentRow["Sujet"] : currentRow["Verbe"];
            oldName = encode(oldName);
            var newName = encode(prompt("["+ decode(oldName) + "] va \352tre renomm\351e en :"));
            
            // On verifie que le nouveau nom est non-vide et non identique
            if(!newName){return;}
            if(newName == oldName){return;};
            
            // On verifie si le nouveau nom n'existe pas deja
            var response = execute('select from index:'+type+' where key=\'' + newName +'\' limit 1');
            if(response.result[0]){
                  alert('Cette ' +typeName + ' est d\351j\340 pr\351sente. Si vous souhaitez faire une fusion, utilisez le bouton Merge');
                  return;
            }
            
            var query = 
                  typeIsSubject ? 'update ographvertex set name = \'' + newName+'\' where name =\''+oldName+'\'' : 'update ographedge set label = \'' + newName+'\' where label =\''+oldName+'\'';
            
            execute(query);
            
            if(typeIsSubject){
                  $("#queryResultTable").jqGrid('setCell',selectedRow,'Sujet', decode(newName));
                  commandResponse.result[selectedRow - 1]["Sujet"]= decode(newName);
            }
            else {
                  for(var row in commandResponse.result){
                        currentRow = commandResponse.result[row];
                        if(currentRow["Verbe"]== decode(oldName)) {
                              $("#queryResultTable").jqGrid('setCell',parseInt(row) +1,'Verbe', decode(newName));
                              currentRow["Verbe"]= decode(newName);
                        }
                  }
            }
            
            clearOutput();
            printMessage(new Date(), " - INFO - ["+typeName+"] \""+decode(oldName)+"\" renomm\351e en \""+ decode(newName)+"\"");
            
      }
      catch(e){
            alert(e.message);
      }
}

//Supprime les relations selectionnees (utilise dans le panel de modif des relations)
function deleteRelations() {

      if(!canUserWrite()) { 
            alert("Vous n'avez pas les droits suffisants.");
            return;
      }

      var selected = getSelectedRows();
      if (noRowsSelected(selected, "relation")) {return;}
      
      try{
      
            var listeRidSel=[];
            var listeRidInv= [];
            var listeRel = [];
            
            var currentRow ;
            
            // On recupere la liste des identifiants des relations selectionnees et celle de leurs relations inverses
            for(var rowIndex in selected){
            
                  currentRow =commandResponse.result[selected[rowIndex]-1];
                  
                  listeRidSel.push(currentRow["rid"]);
                  listeRidInv.push(currentRow["invRid"]);
                  listeRel.push({	sujet: currentRow["Sujet"], 
                                          verbe: currentRow["Verbe"],
                                          complement:currentRow["Complement"]});
            }

            listeRidInv = removeFromList(listeRidInv, listeRidSel);
            // on a la liste des rids des relations inverses a supprimer non selectionnees
            
            for(var row in commandResponse.result){
                  currentRow = commandResponse.result[row];
                  if(listeRidInv.indexOf(currentRow["rid"])>-1){
                        selected.push(parseInt(row)+1);
                        listeRel.push({	sujet: currentRow["Sujet"], 
                                          verbe: currentRow["Verbe"],
                                          complement:currentRow["Complement"]});
                  }
            }
            
            // On demande confirmation
            var listeRelations = "";
            var currentRel;
            for(var rel in listeRel){
                  currentRel = listeRel[rel]
                  listeRelations+= "\n" + currentRel.sujet + " " + currentRel.verbe + " " + currentRel.complement 
            }
            if(!confirm("Voulez-vous vraiment supprimer les relations : \n"+ listeRelations)){
                  return;
            }
            
            //TODO : a ameliorer ! On fait une requete pour chaque relation a supprimer !!
            deleteEdgesFromList(listeRidSel);
            deleteEdgesFromList(listeRidInv);
            
            clearOutput();
            printMessage(new Date(), " - INFO - "+ "Les relations suivantes ont \351t\351 supprim\351es :"+listeRelations);
            
            // On supprime visuellement les lignes supprimes et on vide la selection
            deleteSelectedRowsInGrid(selected);
            selArr= [];
      }
      catch(e){
            alert(e.message);
      }
}

// Supprime les entites selectionnees (utilise dans le panel de modif des entites)
function deleteEntities() {
      
      if(!canUserWrite()) { 
            alert("Vous n'avez pas les droits suffisants.");
            return;
      }
      
      var selected = getSelectedRows();
      if (noRowsSelected(selected, "entit\351")) {return;}
      
      try{
      
            var listeRid=[];
            var listeSujets=[];
            var currentRow ;
            
            // On fait la liste des sujets et de leurs identifiants
            for(var rowIndex in selected){
                  currentRow =commandResponse.result[selected[rowIndex]-1];
                  listeRid.push(currentRow["rid"]);
                  listeSujets.push(currentRow["Sujet"]);
            }
            
            // On demande confirmation
            if(	selected.length ==1 && !confirm("Voulez-vous vraiment supprimer l'entit\351 "+ listeSujets[0] +" et ses relations ?")){
                  return;
            }
            if(	selected.length > 1 && !confirm("Voulez-vous vraiment supprimer les entit\351s "+ listeSujets.join(', ') +" et leurs relations ?")){
                  return;
            }
            
            var nbRelationsDeleted = 0;
            
            //on recupere les rids des edges a supprimer. On les supprime un par un et on supprime le sujet
            //TODO: a changer absolument, performances nulles !
            var edgesList = (execute('find references (select from ['+ listeRid +'])')).result;
            if(!edgesList) {
                  alert("Une erreur est survenue");
                  return;
            }
            for(i in edgesList) {
                  for (j in edgesList[i].referredBy) {
                        execute('delete edge '+edgesList[i].referredBy[j]);
                        nbRelationsDeleted ++;
                  }
            }
            
            execute('delete from ['+listeRid+']');
            
            //On affiche la trace
            clearOutput();
            var deletedMessages = (selected.length ==1) ? "L'entit\351 "+listeSujets.join(', ')+" et ses " : "Les entit\351s "+listeSujets.join(', ')+ " et leurs ";
            printMessage(new Date(), " - INFO - "+ deletedMessages +nbRelationsDeleted +" relations ont \351t\351 supprim\351es");
            
            // On supprime visuellement les lignes supprimes et on vide la selection
            deleteSelectedRowsInGrid(selected);
            selArr=[];
      }
      catch(e){
            alert(e.message);
      }
}


// Fusionne plusieurs entites en une seule
function mergeEntities(){
            
      if(!canUserWrite()) { 
            alert("Vous n'avez pas les droits suffisants.");
            return;
      }

      var selected = getSelectedRows();
      if (selected.length <=1) {
            alert("Vous devez choisir au moins deux entit\351s \340 fusionner !");
            return;
      }
      
      try{
      
            var listeSuj=[];
            var listeRid=[];
            var lastNodeRid = commandResponse.result[selected[0]-1]["rid"];
            var currentRow;
            
            // on recupere la liste des sujets et des identifiants des entites a fusionner (sauf le premier=noeud fusionne)
            for(rowIndex in selected){
                  currentRow =commandResponse.result[selected[rowIndex]-1];
                  listeSuj.push(currentRow["Sujet"]);
                  if(rowIndex > 0) {
                        listeRid.push(currentRow["rid"]);
                  }
            }
            
            var newRelationName = encode(askNewMergedName('entit\351', listeSuj));
            
            // Si le nouveau nom est vide, on sort
            if(!newRelationName){return;}
            
            // La nouvelle entite ne doit pas exister
            var response = execute('select from index:subject where key=\''+newRelationName+'\'').result;
            if(response[0] && (response[0].rid != lastNodeRid) && ($.inArray(response[0].rid,listeRid) == -1) ){
                  alert('Cette entit\351 existe. Pour fusionner, s\351lectionnez aussi cette entit\351');
                  return;
            }
            
            // On recupere les relations des autres entites que l'on devra ajouter a l'entite finale
            var edgesIn = execute('select from (traverse v.in from [' + listeRid+']) where @class = \'OGraphEdge\'').result;
            var edgesOut = execute('select from (traverse v.out from [' + listeRid+']) where @class = \'OGraphEdge\'').result;
            
            // On verifie que les requetes n'ont pas echouees
            if(!edgesIn && !edgesOut){
                  alert('Une erreur est survenue');
                  return;
            }
            
            //A optimiser : on fait un update pour chaque lien a faire
            addEdgeLinksToNode(edgesIn, lastNodeRid, "in");
            addEdgeLinksToNode(edgesOut, lastNodeRid, "out");
            
            execute('update ographedge set in = ' + lastNodeRid+' where in in ['+listeRid.join(',')+']');
            execute('update ographedge set out = ' + lastNodeRid+' where out in ['+listeRid.join(',')+']');
            execute('delete from ['+listeRid.join(',')+']');
            execute('update '+lastNodeRid+' set name=\''+newRelationName+'\'');
            
            $("#queryResultTable").jqGrid('setCell',selected[0],'Sujet', decode(newRelationName));
            commandResponse.result[selected[0]-1]["Sujet"] = decode(newRelationName);
            selArr=[selected.shift()];
            deleteSelectedRowsInGrid(selected);
            
            clearOutput();
            printMessage(new Date(), " - INFO - Les entit\351s suivantes ont \351t\351 fusionn\351es en \""+ decode(newRelationName)+"\"");	
            for(var rowIndex in listeSuj){
                  $("#output").val($("#output").val()+"\n                  "+listeSuj[rowIndex]);
            }
            
      }
      catch(e){
            alert(e.message);
      }
}


// Fusionne plusieurs relations en une nouvelle
function mergeRelations() {

      if(!canUserWrite()) { 
            alert("Vous n'avez pas les droits suffisants.");
            return;
      }

      var selected = getSelectedRows();;
      if (selected.length <=1) {
            alert("Vous devez choisir au moins deux relations \340 fusionner !");
            return;
      }
      
      try {
            var listeVerb=[];
            var listeVerbForQuery=[];
            var currentVerb;
            for(var rowIndex in selected){
                  currentVerb = commandResponse.result[selected[rowIndex]-1]["Verbe"];
                  listeVerb.push(currentVerb);
                  listeVerbForQuery.push(encode(currentVerb));
            }
            
            var newRelationName = encode(askNewMergedName('relation', listeVerb));
            
            // Si le nouveau nom est vide, on sort
            if(!newRelationName){return;}
            
            // On demande confirmation lorsque le nom saisi existe en base et n'a pas ete selectionne
            if(listeVerb.indexOf(decode(newRelationName)) <= -1){
                  var response = execute('select from ographedge where label=\''+newRelationName+'\' limit 1').result;
                  if(response[0]){
                        if(!confirm('Cette relation existe d\351j\340 en base, \352tes-vous s\373r de vouloir continuer ?')) {return;}
                  }
            }

            // on met a jour le nom des relations selectionnees
            execute('update ographedge set label = \'' + newRelationName+'\' where label in [\''+listeVerbForQuery.join('\',\'')+'\']');
                  
            for(var row in commandResponse.result){
                  currentRow = commandResponse.result[row];
                  if(listeVerb.indexOf(currentRow["Verbe"]) > -1) {
                        $("#queryResultTable").jqGrid('setCell',parseInt(row) +1,'Verbe', decode(newRelationName));
                        currentRow["Verbe"]= decode(newRelationName);
                  }
            }
            
            clearOutput();
            printMessage(new Date(), " - INFO - Les relations suivantes ont \351t\351 fusionn\351es en \""+ decode(newRelationName)+"\"");
                  for(var rowIndex in listeVerb){
                        $("#output").val($("#output").val()+"\n                  "+listeVerb[rowIndex]);
                  }
      }
      catch(e){
            alert(e.message);
      }
}
