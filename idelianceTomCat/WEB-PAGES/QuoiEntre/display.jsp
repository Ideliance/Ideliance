<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
	<style>
	div.sidebar-nav{
		display: none;
	}
	</style>
</head>

<c:set var="resultsTable" value="${requestScope.resultsTable}" scope="page" />

<content tag="additionalJavascript">
	<script type="text/javascript" src="/public/js/arbor/arbor.js"></script>
	<script type="text/javascript" src="/public/js/arbor/arbor-tween.js"></script>
	<script type="text/javascript" src="/public/js/arbor/arbor-graphics.js"></script>
	<script type="text/javascript">
	
//
//  js from arbor.js and extended
//
 var sys;
(function($){

  var Renderer = function(canvas){
    var canvas = $(canvas).get(0)
    var ctx = canvas.getContext("2d");
    var particleSystem

    var gfx = arbor.Graphics(canvas);

    var that = {
      init:function(system){
        //
        // the particle system will call the init function once, right before the
        // first frame is to be drawn. it's a good place to set up the canvas and
        // to pass the canvas size to the particle system
        //
        // save a reference to the particle system for use in the .redraw() loop
        particleSystem = system

        // inform the system of the screen dimensions so it can map coords for us.
        // if the canvas is ever resized, screenSize should be called again with
        // the new dimensions
        particleSystem.screenSize(canvas.width, canvas.height) 
        particleSystem.screenPadding(80) // leave an extra 80px of whitespace per side
        
        // set up some event handlers to allow for node-dragging
        that.initMouseHandling()
      },
      
      redraw:function(){
        // 
        // redraw will be called repeatedly during the run whenever the node positions
        // change. the new positions for the nodes can be accessed by looking at the
        // .p attribute of a given node. however the p.x & p.y values are in the coordinates
        // of the particle system rather than the screen. you can either map them to
        // the screen yourself, or use the convenience iterators .eachNode (and .eachEdge)
        // which allow you to step through the actual node objects but also pass an
        // x,y point in the screen's coordinate system
        // 
        ctx.fillStyle = "white"
        ctx.fillRect(0,0, canvas.width, canvas.height)
        
        particleSystem.eachEdge(function(edge, pt1, pt2){
          // edge: {source:Node, target:Node, length:#, data:{}}
          // pt1:  {x:#, y:#}  source position in screen coords
          // pt2:  {x:#, y:#}  target position in screen coords

          /*
          ** Draw the link between 2 node : line + 2 arrows
          */
          // draw a line from pt1 to pt2
          ctx.strokeStyle = "rgba(0,0,0, .333)"
          ctx.lineWidth = 2
          ctx.beginPath()
          ctx.moveTo(pt1.x, pt1.y)
          ctx.lineTo(pt2.x, pt2.y)
          ctx.stroke()
          ctx.beginPath()
          
          //Draw 2 triangles as arrows
          slope = (pt2.y-pt1.y)/(pt2.x-pt1.x); // get the slope of the line between pt1 and pt2
          triangleBaseLineSize = Math.sqrt(25/(1/(slope*slope)+1)) // constant to normalize line size
          triangleSideLineSize = Math.sqrt(100/(slope*slope+1)) // constant to normalize line size
          
          //orientation is needed to keep the arrow always in the same way
          if(pt2.x-pt1.x>0)
        	  orientation = 1
       	  else
          	  orientation = -1
        		  
		  //q1 : position of the first quarter on the line between pt1 and pt2
       	  q1x = pt1.x+(pt2.x-pt1.x)/4
          q1y = pt1.y+(pt2.y-pt1.y)/4

          //drawing first triangle
          ctx.moveTo(q1x+triangleSideLineSize*orientation, q1y+triangleSideLineSize*orientation*slope)
          ctx.lineTo(q1x-triangleBaseLineSize, q1y+triangleBaseLineSize*(1/slope))
          ctx.lineTo(q1x+triangleBaseLineSize, q1y-triangleBaseLineSize*(1/slope))
          ctx.lineTo(q1x+triangleSideLineSize*orientation, q1y+triangleSideLineSize*orientation*slope)
          ctx.fill()
          ctx.stroke()
          
          //q3 : position of the third quarter 
          q3x = pt1.x+3*(pt2.x-pt1.x)/4
          q3y = pt1.y+3*(pt2.y-pt1.y)/4

          //drawing second triangle
          ctx.moveTo(q3x+triangleSideLineSize*orientation, q3y+triangleSideLineSize*orientation*slope)
          ctx.lineTo(q3x-triangleBaseLineSize, q3y+triangleBaseLineSize*(1/slope))
          ctx.lineTo(q3x+triangleBaseLineSize, q3y-triangleBaseLineSize*(1/slope))
          ctx.lineTo(q3x+triangleSideLineSize*orientation, q3y+triangleSideLineSize*orientation*slope)
          ctx.fill()
          ctx.stroke()
          
          w = 120;
          gfx.rect((pt2.x+pt1.x)/2-w/2, (pt2.y+pt1.y)/2-8, w, 20, 4, {fill:"#0088CC", alpha:"0"})
          
          gfx.text(edge.data.relation, (pt2.x+pt1.x)/2, (pt2.y+pt1.y)/2+5, {color:"#0088CC", align:"center", font:"Arial", size:9});
        })

        particleSystem.eachNode(function(node, pt){
          // node: {mass:#, p:{x,y}, name:"", data:{}}
          // pt:   {x:#, y:#}  node position in screen coords
        	var w;
            if(node.data.use === 'subject'){
                w = 120;
				gfx.rect(pt.x-w/2, pt.y-8, w, 20, 4, {fill:node.data.color, alpha:node.data.alpha})
                gfx.text(node.data.label, pt.x, pt.y+5, {color:"white", align:"center", font:"Arial", size:9});
            }
        })    			
      },
      
      initMouseHandling:function(){
        // no-nonsense drag and drop (thanks springy.js)
        var dragged = null;

        // set up a handler object that will initially listen for mousedowns then
        // for moves and mouseups while dragging
        var handler = {
          clicked:function(e){
        	  var pos = $(canvas).offset();
	          _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
	          dragged = particleSystem.nearest(_mouseP);
        	  if(!e.shiftKey) {
	            if (dragged && dragged.node !== null){
	              // while we're dragging, don't let physics move the node
	              dragged.node.fixed = true
	            }
	
	            $(canvas).bind('mousemove', handler.dragged)
	            $(window).bind('mouseup', handler.dropped)
        	  }
        	  else{
        		  // Click + Shift = remove the node
        		  sys.pruneNode(dragged.node);
        		  // After removing a node, delete the nodes without edges
        		  sys.eachNode(testNbEdges);
        	  }
            return false
          },
          dblclicked:function(e){
        	  //dblclick = get nodes in relation with the node double clicked
        	  var pos = $(canvas).offset();
              _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
              dragged = particleSystem.nearest(_mouseP);
        	  
          	   if(dragged.distance < 65)
    				getNearSubjectBySubjectId(dragged.node.data.subjectId);
          	  	
          },
          dragged:function(e){
            var pos = $(canvas).offset();
            var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

            if (dragged && dragged.node !== null){
              var p = particleSystem.fromScreen(s)
              dragged.node.p = p
            }

            return false
          },

          dropped:function(e){
            if (dragged===null || dragged.node===undefined) return
            if (dragged.node !== null) dragged.node.fixed = false
            dragged.node.tempMass = 1000
            
           
       	   
            dragged = null
            $(canvas).unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            _mouseP = null
            return false
          }
        }
        
        // start listening
        $(canvas).mousedown(handler.clicked);
        $(canvas).dblclick(handler.dblclicked);

      },
      
    }
    return that
  }
  function testNbEdges(node){
	  //Test a node and delete it if no edges are found
	  if(sys.getEdgesTo(node).length == 0 && sys.getEdgesFrom(node).length == 0){
		  sys.pruneNode(node);
	  }
  }
  function getNearSubjectBySubjectId(subjectId){
	//Get all nodes linked to the subject 'subjectId' by an AJAX request
	$.ajax({
		type : "POST",
		url : "/ajax/getSubjectsNextTo",
		data : "subjectId=" + subjectId,
		dataType: 'json',
	  	complete : function(e){
	  		var jsonResponse = $.parseJSON(e.responseText);
	  		var confirmLength = true;
	  		//If length > 10, ask the user if he really want to display all nodes 
	  		if(jsonResponse.length>10)
	  			confirmLength = confirm("Display "+jsonResponse.length+" nodes ?");
	   	for(var i=0; i<jsonResponse.length; i++)
	   	{
	   		if(confirmLength){
	   			// If all nodes has to be displayed (either there is < 10 node or the user confirmed)
	   			sys.addNode("S"+jsonResponse[i].subject.id,{'use':'subject','color':'#333333','label':jsonResponse[i].subject.entitled,'wordlength':25,'subjectId':jsonResponse[i].subject.id});	
	   			if(sys.getEdges("S"+jsonResponse[i].subject.id,"S"+subjectId).length == 0)
	   				//Don't add an edge if there is already an edge
	   				sys.addEdge("S"+subjectId,"S"+jsonResponse[i].subject.id,{'relation':jsonResponse[i].relation.entitled})
	   		}else{
	   			// If the user didn't confirm displaying all nodes, just add edge between existings nodes
	   			if(sys.getNode("S"+jsonResponse[i].subject.id) != undefined){
	   				if(sys.getEdges("S"+jsonResponse[i].subject.id,"S"+subjectId).length == 0)
		   				//Don't add an edge if there is already an edge
	   					sys.addEdge("S"+subjectId,"S"+jsonResponse[i].subject.id,{'relation':jsonResponse[i].relation.entitled})
	   			}
	   		}
	   	}
	   }
	});
  }

  $(document).ready(function(){
   sys = arbor.ParticleSystem(100, 600, 0.5) // create the system with sensible repulsion/stiffness/friction
    sys.parameters({gravity:true}) // use center-gravity to make the graph settle nicely (ymmv)
    sys.renderer = Renderer("#viewport") // our newly created renderer will have its .init() method called shortly by sys...
    <c:set var="i" value="0" scope="page" />
    //Adding the nodes & edges on load
   	<c:forEach var="tabLine" items="${resultsTable}">
   		<c:set var="previousSubject" value="" scope="page" />
		<c:forEach var="item" items="${tabLine}">
			<c:choose>
				<c:when test="${item.getRelation() != Null}">
				// when adding the first node : no edge required
					sys.addNode("S${item.getSubject().getId()}",{'use':'subject','color':'#333333','label':"${item.getSubject().getEntitled(lang)}",'wordlength':25,'subjectId':"${item.getSubject().getId()}"});
					<c:choose>
						<c:when test="${previousSubject != ''}">
							// link the new node to the previous node in the same line of 'resultsTable'
							sys.addEdge("${previousSubject}","S${item.getSubject().getId()}",{'relation':"${item.getRelation().getEntitled(lang)}"})
						</c:when>
					</c:choose>
				</c:when>
				<c:otherwise>
					sys.addNode("S${item.getSubject().getId()}",{'use':'subject','color':'#006E2E','label':"${item.getSubject().getEntitled(lang)}",'wordlength':25,'subjectId':"${item.getSubject().getId()}"});
				</c:otherwise>
			</c:choose>
			<c:set var="previousSubject" value="S${item.getSubject().getId()}" scope="page" />
		</c:forEach>
	</c:forEach>
  })

})(this.jQuery)
</script>
</content>
<div class="page-header">
	<h1><c:out value="${requestScope.title}" /> <a class="btn" data-content="<strong>Navigate :</strong> double-click on any node<br/><strong>Delete :</strong> SHIFT + click<br/><strong>Caution : don't display too much nodes !</strong>" data-original-title="Help" rel="popover" href="#">
Help
</a></h1>
	
</div>


  <canvas id="viewport" width="1000" height="600"></canvas>
  
<c:set var="resultsTable" value="${requestScope.resultsTable}" scope="page" />

