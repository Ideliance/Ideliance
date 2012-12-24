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
	
///////////////////////////////// Fonctions utilitaires transverses ////////////////////////

// Execute la requete donnee en argument
function execute(query){
      return orientServer.executeCommand(query);
}

// Convertit la chaine de la partie JS au bon format serveur
function toUTF8(string){
      return orientServer.UTF8Encode(string);
}

// Convertit dans le sens inverse
function fromUTF8(string){
      return orientServer.UTF8Decode(string);
}

// Permet d'echapper les caracteres speciaux
function addSlashes(ch) {
      ch = ch.replace(/\\/g,"\\\\")
      ch = ch.replace(/\'/g,"\\'")
      ch = ch.replace(/\"/g,"\\\"")
      return ch
}

// Fait l'operation inverse
function removeSlashes(ch) {
      ch = ch.replace(/\\\\/g,"\\")
      ch = ch.replace(/\\'/g,"\'")
      ch = ch.replace(/\\\"/g,"\"")
      return ch
}

// Echappe les caracteres speciaux et convertit au bon format
function encode(string){
      return toUTF8(addSlashes($.trim(string)));
}

// Fait l'operation inverse
function decode(string){
      return removeSlashes(fromUTF8(string));
}

//Retire les elements de la liste list2 presents dans list1
function removeFromList(list1, list2){
      var listeFinale = [];
      for(var i=0; i<list1.length; i++) {
            if(list2.indexOf(list1[i])<=-1) {
                  listeFinale.push(list1[i]);
            }
      }
      return listeFinale;	
}

//Ajoute un message dans la console avec precision de la date de l'evenement
function printMessage(date, message){
      var jumpLine='';
      if ($("#output").val() != '') {
            jumpLine= $("#output").val() + "\n";	
      }
      $("#output").val(jumpLine+ date.toLocaleTimeString() + message);
}

//initialise l'autocompletion sur les champs input1, input2. queryBegin est le debut de la requete fournissant les suggestions
function autocomplete(input1, input2, queryBegin){
      $( "#"+input1+", #"+input2).autocomplete({
      source: function( request, response ) {
                  var term = encode(request.term.toLowerCase());
                  var matchingSubjects = execute(queryBegin+' like \'%'+ term +'%\'');
                  response(filterBeginningElemFirst($.map( matchingSubjects.result, function( item ) {
                      return {
                          label: item.name, 
                          value: item.name
                      }
                  }), term).slice(0,5));
            },
      minLength: 2,
            delay: 250,
            autoFocus : true
  });
}

/* Les fonctions suivantes permettent de gerer les selections sur plusieurs pages dans les tableaux 
(code recupere de http://www.cutterscrossing.com/index.cfm/2012/1/4/Intro-to-jqGrid-Part-4-Event-Handling) */

// handler de la selection d'une ligne
var rowSelectionHandler = function (id, status) {
      selectionManager(id, status);
};

// handler de la selection de tout les elements
var selectAllHandler = function (idArr, status) {
      selectionManager(idArr, status);
};

// met a jour la liste des elements selectionnes (
var selectionManager = function (id, status) {
      // was it checked (true) or unchecked (false)
      if(status){
            // if it's just one id (not array)
            if(!$.isArray(id)){
                  // if it's not already in the array, then add it
                  if($.inArray(id,selArr) < 0){selArr.push(id)}
            } else {
                  // which id's aren't already in the 'selected' array
                  var tmp = $.grep(id,function(item,ind){
                        return $.inArray(item,selArr) < 0;
                  });
                  // add only those unique id's to the 'selected' array
                  $.merge(selArr,tmp);
            }
      } else {
            // if it's just one id (not array)
            if(!$.isArray(id)){
                  // remove that one id
                  selArr.splice($.inArray(id,selArr),1);
            } else {
                  // give me an array without the 'id's passed
                  // (resetting the 'selected' array)
                  selArr = $.grep(selArr,function(item,ind){
                        return $.inArray(item,id) > -1;
                  },true);
            }
      }
};

// A chaque changement de page, on reselectionne visuellement 
// les elements qu'on avait precedemment selectionnes (effaces par defaut dans jqGrid)
var gridLoadInit = function () {
      // if the 'selected' array has length
      // then loop current records, and 'check'
      // those that should be selected
      if(selArr.length > 0){
            var tmp = $("#queryResultTable").jqGrid('getDataIDs');
            $.each(selArr, function(ind, val){
                  var pos = $.inArray(val, tmp);
                  if(pos > -1){
                        $("#queryResultTable").jqGrid('setSelection',val);
                  }
            });
       }
};