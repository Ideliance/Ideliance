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

// Effectue la recherche de relations entre les deux sujets
function beginQE(){

      var sujet1 = encode($("#sujet1").val());
      var sujet2 = encode($("#sujet2").val());


      //On verifie que la profondeur, et que les deux sujets renseignes sont correctes
      var depthLimit = parseInt($("#depthLimit").val());
      if(!depthLimit) {
            alert("La profondeur saisie est incorrecte.");
            return;
      }
      if(!sujet1 || !sujet2) {
            alert("Les deux sujets doivent \352tre saisis.");
            return;
      }
      
      var forbiddenRelations = [];
      $('input:checked[name=relations]').each(function() {
            forbiddenRelations.push(encode($(this).val()));
      });
      
      //On execute la requete gremlin+sql et on recupere le resultat
      var queryResult = execute('select gremlin( \'current.outE.filter{!([\''+forbiddenRelations.join('\',\'')+'\'].contains(it.label))}.inV.simplePath().loop(4){it.loops < '+depthLimit+'}{it.object.name == \''+sujet2+'\'}.simplePath().path{it.name}{it.label}\') from V where name = \''+sujet1+'\'');				
      
      // Une reponse doit avoir ete obtenue
      if(!queryResult.result){
            alert("Une erreur est survenue");
            return;
      }
      
      // Si la reponse est vide, c'est qu'aucun chemin n'a ete trouve
      if(!queryResult.result[0]){
            $("#pathsFound").html('<h4>Pas de chemins trouv&eacute;s</h4>');
            clearOutput();
            printMessage(new Date(), " - INFO - No paths found.");
            return;
      }
      
      var paths = queryResult.result[0].gremlin;
      $("#pathsFound").html('<h3>Chemin(s) trouv&eacute;(s)</h3>');
      $("#pathsFound").append('<table width="100%" cellpadding="4" cellspacing="3">');
      
      // On traite le cas ou plusieurs chemins ont ete trouves
      if(paths[0] instanceof Array){
            for(i in paths){
                  $("#pathsFound").append("<tr>");
                  for(j in paths[i]){
                        $("#pathsFound").append('<td><input type= "text" class="pathElem" disabled value ="'+paths[i][j]+'"></input></td>');
                  }
                  $("#pathsFound").append("</tr>");
                  
            }
            clearOutput();
            printMessage(new Date(), " - INFO - Found "+paths.length+" paths.");
      }
      // Cas ou un seul chemin a ete trouve (la reponse de la requete est une liste simple et non une liste de liste)
      else {
            $("#pathsFound").append("<tr>");
            for(j in paths){
                  $("#pathsFound").append('<td><input type= "text" class="pathElem" disabled value ="'+paths[j]+'"></input></td>');
            }
            $("#pathsFound").append("</tr>");
            clearOutput();
            printMessage(new Date(), " - INFO - Found one path");
      }
      $("#pathsFound").append("</table>");
      
      $(".pathElem").each(function(){
            $(this).css({
                              "font-family" : "Courier New",
                              "width" : $(this).val().length*8+"px"
                              });
      });
      
}

function loadRelationsToCross(){
      
      var queryResult = execute("select distinct(label) as label from ographedge order by label");				
                  
      // Une reponse doit avoir ete obtenue
      if(!queryResult.result){
            alert("Une erreur est survenue");
            return;
      }
      
      // Si la reponse est vide, c'est qu'aucun chemin n'a ete trouve
      if(!queryResult.result[0]){
            $("#relationsToCross").html('<input type ="text" value= "Aucune relations disponibles" disabled/>');
            return;
      }
      
      var listVerbs = queryResult.result;
      var currentVerb;
      
      for (i in listVerbs){
            currentVerb = listVerbs[i].label;
            $("#relationsToCross").append('<label for="'+currentVerb+'"><input type="checkbox" name="relations" id="'+currentVerb+'" value="'+currentVerb+'">'+currentVerb+'</label>');
      }
      
      $("#relationsToCross").css('border-style','groove').css('height','100').css('width', '300');
      $("#relationsToCross").css('overflow', 'auto');
      
}