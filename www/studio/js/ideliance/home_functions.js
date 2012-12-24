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

function getWordCountFor(type){

      if(type != 'relations' && type != 'entities'){
            return null;
      }
      
      var labelList;
      if(type == 'relations'){
            labelList = execute('select from (select list(label) from e) limit 1');
      }
      else {
            labelList = execute('select from (select list(in.name) from e) limit 1');
      }
      if(!labelList || !labelList.result[0]) {
            return []; 
      }
      var list = labelList.result[0].list ;
      var wordCount ={};

      for(word in list) {
            var wordCounted = !!(wordCount[list[word]]);
            wordCount[list[word]] = wordCounted ? wordCount[list[word]] + 1 : 1 ;
      }

      var words =[];
      for(word in wordCount){
            words.push({text: word, size: (wordCount[word])*10});
      }
      return words;
}

function drawRelationsCloud(words) {
      draw(words, "#cloudRelations");
}

function drawEntitiesCloud(words) {
      draw(words, "#cloudEntities");
}

function draw(words, id) {
      var fill = d3.scale.category20();

      d3.select(id).append("svg")
            .attr("width", 400)
            .attr("height", 400)
        .append("g")
            .attr("transform", "translate(200,200)")
        .selectAll("text")
            .data(words)
        .enter().append("text")
            .style("font-size", function(d) { return d.size + "px"; })
            .style("font-family", "Helvetica")
            .style("fill", function(d, i) { return fill(i); })
            .attr("text-anchor", "middle")
            .attr("transform", function(d) {
              return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
            })
            .text(function(d) { return d.text; });
}

function constructCloud(type){
      if(type != 'entities' && type != 'relations'){
            return;
      }
      var isEntityType = type == 'entities';
      var words = isEntityType ? getWordCountFor('entities') : getWordCountFor('relations');
      var drawFunction = isEntityType ? drawEntitiesCloud : drawRelationsCloud ;
      
      if(words==[]) {return;}
      
      d3.layout.cloud()
        .size([400, 400])
        .timeInterval(10)
        .padding(1)
        .words(words)
        .text(function(d) { return d.text; })
        .rotate(function(d) { return 0 ;})
        .fontSize(function(d) { return d.size*1.5; })
        .on("end", drawFunction)
        .start();
}