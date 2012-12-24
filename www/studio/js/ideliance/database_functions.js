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

// Teste si la classe OGraphVertex contient un attribut name et si la classe OGraphEdge contient un attribut label et inv
// les cree sinon
function createSchemaIfNotExisting(){

      var vertexClass = orientServer.getClass('OGraphVertex');
      var edgeClass = orientServer.getClass('OGraphEdge');
      
      var nameExists = false;
      var labelExists = false;
      var invExists = false;
      var creationTimeExists = false;
      
      for(var i =0, size = vertexClass.properties.length ; i< size ; i++){
            if(vertexClass.properties[i].name == 'name') {nameExists = true;}
      }
      for(var i =0, size = edgeClass.properties.length ; i< size ; i++){
            if(edgeClass.properties[i].name == 'label') {labelExists = true;}
            if(edgeClass.properties[i].name == 'inv') {invExists = true;}
            if(edgeClass.properties[i].name == 'createdTime') {creationTimeExists = true;}
      }

      if(!nameExists) {execute('create property ographvertex.name string');}
      if(!labelExists) {execute('create property ographedge.label string');}
      if(!invExists) {execute('create property ographedge.inv link ographedge');}
      if(!creationTimeExists) {execute('create property ographedge.createdTime datetime ographedge');}
}

// Teste si un index unique sur les sujets et un index non unique sur les verbes existent, elle les cree sinon
function createIndexesIfNotExisting(){

      var listIndex= execute('select flatten(indexes) from cluster:0');
      var indexSubjectFound = false;
      var indexVerbFound = false;
      
      if (listIndex.result[0]){
            // on parcourt la liste des indexes pour voir si subject et verb existent, et on les cree sinon
            for( var i =0, size = listIndex.result.length ; i< size ; i++) {
                  if(listIndex.result[i].name == 'subject') {
                        indexSubjectFound = true;
                  }
                  if(listIndex.result[i].name == 'verb') {
                        indexVerbFound = true;
                  }
                  if(indexSubjectFound && indexVerbFound) {break;}
                  if(i == size-1) {
                        if(!indexSubjectFound) {
                              execute('create index subject on OGraphVertex (name) unique');
                        }
                        if(!indexVerbFound) {
                              execute('create index verb on OGraphEdge (label) notunique');
                        }
                  }
            }
      }
}

// Renvoie le role de l'utilisateur passé en arguments
function getCurrentUserRole(){
      var currentUser = orientServer.getDatabaseInfo().currentUser;
      if(!currentUser) {return null;}
      
      var users = orientServer.securityUsers();
      if(!users) {return null;}
      
      var roles = null;
      for(i in users){
            if(users[i].name == currentUser){
                  roles = users[i].roles;
            }			
      }
      return roles;
}

// Renvoie true si l'utilisateur courant a les droits ecriture
function canUserWrite(){
      var role = getCurrentUserRole();
      
      if(!role) { return null;}
      
      if(role.indexOf('reader') <= -1) {return true;}
      return false;
}