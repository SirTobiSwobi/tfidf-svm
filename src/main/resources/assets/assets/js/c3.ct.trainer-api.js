function getIds(array){
	var ids = new Array();
	for(var i=0;i<array.length;i++){
		ids.push(array[i].id);
	}
	return ids;
}

function getIdObject(){
	
	var pageURL = window.location.search.substring(1);
	var urlVariables = pageURL.split('&');
	var assId, docId, catId, relId, confId, modId;
	for(var i=0; i< urlVariables.length; i++){
		var paramName = urlVariables[i].split('=');
		if(paramName[0] == "assId"){
			assId = paramName[1];
		}else if(paramName[0] == "docId"){
			docId = paramName[1];
		}else if(paramName[0] == "catId"){
			catId = paramName[1];
		}else if(paramName[0] == "relId"){
			relId = paramName[1];
		}else if(paramName[0] == "confId"){
			confId = paramName[1];
		}else if(paramName[0] == "modId"){
			modId = paramName[1];
		}
	}
	
	var output = {assId: assId, docId: docId, catId: catId, relId: relId, confId: confId, modId: modId};
	return output;
	
}

function deleteFromArray(array, value){
	var index = array.indexOf(value);
	if(index>-1){
		array.splice(index, 1);
	}
	return array;
}

function buildArrayFromIds(allEntries,ids){
	var output = new Array();
	for(var i=0;i<allEntries.length;i++){
		if(ids.includes(allEntries[i].id)){
			output.push(allEntries[i]);
		}
	}
	return output;
}

function getObjectById(allObjects, id){
	var output=null;
	for(var i=0; i<allObjects.length; i++){
		if(allObjects[i].id==id){
			output=allObjects[i];
		}
	}
	return output;
}

function idIsSet(id){
	var returnValue=true;
	if(typeof id == "undefined" || id == null || id == -1){
		returnValue=false;
	}
	return returnValue;
}

function renderDocuments(){
	$("#list").empty();
	$("#list").append("<h2>Available documents:</h2>");
	$.getJSON("../documents",function(json){	
		if(json.documents==null){
			$("#list").append("<h3>There are currently no documents in this microservice. You can add one</h3>");
		}else{
			for (var i=0; i< json.documents.length; i++){
				$("#list").append("<li><a href=\"document.html?docId="+json.documents[i].id+"\">/documents/"+json.documents[i].id+" - "+json.documents[i].label+
						"</a> - <a href=\"assignment.html?docId="+json.documents[i].id+"\" class=\"button next\">Assign to category</a></li>");
			}
		}
	});
}

function renderDocument(docId){
	$("#list").empty();
	$("#list").append("<h2>Available document:</h2>");
	$.getJSON("../documents/"+docId,function(json){
		if(json==null){
			$("#list").append("<h3>The document with id "+docId+" does not exist. You can create it</h3>");
		}else{
			$("#list").append("<h3>Id: "+json.id+": "+json.label+"</h3>");
			$("#list").append("<p>URL: "+json.url+"</p>");
			$("#list").append("<p>");
			$("#list").append(json.content);
			$("#list").append("</p>");
		}
	});
}

function createDocument(form){
	var json = "{ \"documents\":[{\"id\":"+form[0].value+
	",\"label\":\""+form[1].value;
	json = json+"\",\"content\":\"";
	json = json+form[2].value.replace(/(\r\n|\n|\r)/gm," ");;
	json = json+"\",\"url\":\""+form[3].value+"\"}]}";
	console.log(json);
	
	var url="../documents";
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function updateDocument(form,docId){
	var json = "{\"id\":"+form[0].value+
	",\"label\":\""+form[1].value;
	json = json+"\",\"content\":\"";
	json = json+form[2].value.replace(/(\r\n|\n|\r)/gm," ");;
	json = json+"\",\"url\":\""+form[3].value+"\"}";
	console.log(json);
	
	var url="../documents/"+docId;
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'PUT',
	    dataType: 'json',
	    data: json,
	    success: function(data){
			 console.log('succes: '+data);
		}
	 });
}

function renderDocumentUpdate(docId){
	$.getJSON("../documents/"+docId,function(json){
		if(json==null){
			//nothing but empty fields
		}else{
			$("#id").val(json.id);
			$("#label").val(json.label);
			$("#url").val(json.url);
			$("#content").empty();
			$("#content").append(json.content);
		}
	});
}

function uploadDocumentJSON(json){
	var url="../documents";	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function deleteAllDocuments(){
	var url="../documents";	
	var json="";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function deleteDocument(docId){
	var url="../documents/"+docId;
	var json="";
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
			 	console.log('succes: '+data);
		}
	 });
}

function renderCategories(){
	$("#list").empty();
	$("#list").append("<h2>Available categories:</h2>");
	$.getJSON("../categories",function(json){
	
	if(json.categories==null){
		$("#list").append("<h3>There are currently no categories in this microservice. You can add one</h3>");
	}else{
		for (var i=0; i< json.categories.length; i++){
			$("#list").append("<li><a href=\"category.html?catId="+json.categories[i].id+"\">/categories/"+json.categories[i].id+" - "+json.categories[i].label+
			"</a> - <a href=\"assignment.html?catId="+json.categories[i].id+"\" class=\"button next\">Assign documents</a></li>");
			}
		}
	});
}

function createCategory(form){
	var json = "{ \"categories\":[{\"id\":"+form[0].value+
		",\"label\":\""+form[1].value;
	json = json+"\",\"description\":\"";
	json = json+form[2].value.replace(/(\r\n|\n|\r)/gm," ")+"\"}]}";
	var url="../categories";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
			 	console.log('succes: '+data);
		}
	 });
}

function uploadCategoryJSON(json){
	var url="../categories";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('succes: '+data);
		}
	});
}

function deleteAllCategories(){
	var url="../categories";
	var json="";

	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('succes: '+data);
		}
	 });
}

function renderCategory(catId){
	$.getJSON("../categories/"+catId,function(json){
		$("#list").empty();
		$("#list").append("<h2>Available category:</h2>");
		if(json==null){
			$("#list").append("<h3>The category with id "+catId+" does not exist. You can create it</h3>");
		}else{
			$("#list").append("<h3>Id: "+json.id+": "+json.label+"</h3>");
			$("#list").append("<p>Description: "+json.description+"</p>");
			
		}
	});
}

function renderCategoryUpdate(catId){
	$.getJSON("../categories/"+catId,function(json){
		console.log(json);
		if(json==null){
			//nothing but empty fields
		}else{
			$("#id").val(json.id);
			$("#label").val(json.label);
			$("#description").val(json.description);
		}
	});
}

function updateCategory(form){
	var json = "{\"id\":"+form[0].value+
	",\"label\":\""+form[1].value;
	json = json+"\",\"description\":\"";
	json = json+form[2].value.replace(/(\r\n|\n|\r)/gm," ");;
	json = json+"\"}";
	var url="../categories/"+form[0].value;
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'PUT',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	 });
}

function deleteCategory(catId){
	var url="../categories/"+catId;
	var json="";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	});
}

function renderRelationships(){
	var categoryJSON;
	$("#list").empty();
	$("#list").append("<h2>Available category relationships:</h2>");
	$.getJSON("../categories",function(json){
			categoryJSON = json;
		}).done(function(){
			$.getJSON("../relationships",function(json){
				
				if(json.relationships==null){
					$("#list").append("<h3>There are currently no category relationships in this microservice. You can add one</h3>");
				}else{
					for (var i=0; i< json.relationships.length; i++){
						var fromLabel;
						var toLabel;
						for(var j=0; j< categoryJSON.categories.length; j++){
							if(json.relationships[i].fromId==categoryJSON.categories[j].id){
								fromLabel = categoryJSON.categories[j].label;
							}
							if(json.relationships[i].toId==categoryJSON.categories[j].id){
								toLabel = categoryJSON.categories[j].label;
							}
						}
						var appendString = "<li><a href=\"relationship.html?relId=";
						appendString = appendString +json.relationships[i].id+"\">/relationships/"+json.relationships[i].id;
						appendString = appendString +" from: "+json.relationships[i].fromId;
						appendString = appendString +" ("+fromLabel+")";
						appendString = appendString +" to: "+json.relationships[i].toId;
						appendString = appendString +" ("+toLabel+")";
						appendString = appendString +" - Type: "+json.relationships[i].type;
						appendString = appendString +"</a></li>";
						$("#list").append(appendString);
					}
				}
				
			})
	});
}

/*
function renderCategoryCreation(){
	$("#fromId").empty();
	$("#toId").empty();
	$.getJSON("../categories",function(json){
			var appendString = "";
			for(var j=0; j< json.categories.length; j++){
				appendString = appendString +"<option value=\""+json.categories[j].id+"\">";
				appendString = appendString +json.categories[j].id+" (";
				appendString = appendString +json.categories[j].label+")</option>";			
			}
			$("#fromId").append(appendString);
			$("#toId").append(appendString);		
	});
}
*/

function createRelationship(form){
	var json = "{ \"relationships\":[{\"id\":"+form[0].value;
	json = json + ", \"fromId\":"+form[1].value;
	json = json + ", \"toId\":"+form[2].value;
	json = json + ", \"type\": \""+form[3].value+"\"";
	json = json + "}]}";
	console.log(json);
	var url="../relationships";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
			console.log('succes: '+data);
		}
	});
}

function uploadRelationshipJSON(json){
	var url="../relationships";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	 });
}

function deleteAllRelationships(){
	var json="";
	var url="../relationships";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	});
}

function renderRelationship(){
	var relId=getIdObject().relId;
	var categoryJSON;
	
	$("#list").empty();
	$("#list").append("<h2>Available relationship:</h2>");
	$.getJSON("../categories",function(json){
			categoryJSON = json;
		}).done(function(){
			$.getJSON("../relationships/"+relId,function(json){
				console.log(json);					
				var fromLabel;
				var toLabel;
				for(var j=0; j< categoryJSON.categories.length; j++){
					if(json.fromId==categoryJSON.categories[j].id){
						fromLabel = categoryJSON.categories[j].label;
					}
					if(json.toId==categoryJSON.categories[j].id){
						toLabel = categoryJSON.categories[j].label;									}
				}
				var appendString = "<table>";
				appendString = appendString+"<tr><td>Id:</td><td>"+json.id+"</td></tr>";
				appendString = appendString+"<tr><td>From:</td><td><a href=\"category.html?catId="+json.fromId+"\" class=\"button next\">"+json.fromId+" ("+fromLabel+")</a></td></tr>";
				appendString = appendString+"<tr><td>To:</td><td><a href=\"category.html?catId="+json.toId+"\" class=\"button next\">"+json.toId+" ("+toLabel+")</a></td></tr>";
				appendString = appendString+"<tr><td>Type:</td><td>"+json.type+"</td></tr>";
				appendString = appendString+"</table>";
				$("#list").append(appendString);		
				
			}).fail(function(){
				$("#list").empty();
				$("#list").append("<h3>There is currently no category with id "+relId+". You can create it</h3>");
			})
	});
}

function renderRelationshipCreation(){
	var categoryJSON;
	$.getJSON("../categories",function(json){
		categoryJSON = json;
		var appendFrom="";
		var appendTo="";
		var appendType="";
		for(var j=0; j< categoryJSON.categories.length; j++){
			appendFrom = appendFrom +"<option value=\""+categoryJSON.categories[j].id+"\"";
			appendFrom = appendFrom +">"+categoryJSON.categories[j].id+" (";
			appendFrom = appendFrom +categoryJSON.categories[j].label+")</option>";	
			appendTo = appendTo +"<option value=\""+categoryJSON.categories[j].id+"\"";
			appendTo = appendTo +">"+categoryJSON.categories[j].id+" (";
			appendTo = appendTo +categoryJSON.categories[j].label+")</option>";	
			appendType += "<option value=\"Sub\"";
			appendType += ">Sub</option>";
			appendType += "<option value=\"Equality\"";
			appendType += ">Equality</option>";
		}
		$("#fromId").empty();
		$("#toId").empty();
		$("#id").empty();
		$("#type").empty();
		$("#fromId").append(appendFrom);
		$("#toId").append(appendTo);
		$("#id").val(json.id);
		$("#type").val(json.type);
	});
}

function renderRelationshipUpdate(){
	var relId=getIdObject().relId;
	var categoryJSON;
	$.getJSON("../categories",function(json){
		categoryJSON = json;
		}).done(function(){
			$.getJSON("../relationships/"+relId,function(json){
				//console.log(json);
				//console.log(categoryJSON);
				var appendFrom="";
				var appendTo="";
				var appendType="";
				for(var j=0; j< categoryJSON.categories.length; j++){
					appendFrom = appendFrom +"<option value=\""+categoryJSON.categories[j].id+"\"";
					if(json.fromId==categoryJSON.categories[j].id){
						appendFrom = appendFrom+" selected";
					}
					appendFrom = appendFrom +">"+categoryJSON.categories[j].id+" (";
					appendFrom = appendFrom +categoryJSON.categories[j].label+")</option>";	
						
					appendTo = appendTo +"<option value=\""+categoryJSON.categories[j].id+"\"";
					if(json.toId==categoryJSON.categories[j].id){
						appendTo = appendTo+" selected";
					}
					appendTo = appendTo +">"+categoryJSON.categories[j].id+" (";
					appendTo = appendTo +categoryJSON.categories[j].label+")</option>";				
				}
				appendType += "<option value=\"Sub\"";
				if(json.type=="Sub"){
					appendType += " selected";
				}
				appendType += ">Sub</option>";
				appendType += "<option value=\"Equality\"";
				if(json.type=="Equality"){
					appendType += " selected";
				}
				appendType += ">Equality</option>";
				console.log("appendType: "+appendType);
				$("#fromId").empty();
				$("#toId").empty();
				$("#id").empty();
				$("#type").empty();
				$("#fromId").append(appendFrom);
				$("#toId").append(appendTo);
				$("#id").val(json.id);
				$("#relType").val(appendType);
		}).fail(function(){
			var appendFrom="";
			var appendTo="";
			var appendType="";
			for(var j=0; j< categoryJSON.categories.length; j++){
				appendFrom = appendFrom +"<option value=\""+categoryJSON.categories[j].id+"\"";
				appendFrom = appendFrom +">"+categoryJSON.categories[j].id+" (";
				appendFrom = appendFrom +categoryJSON.categories[j].label+")</option>";	
				appendTo = appendTo +"<option value=\""+categoryJSON.categories[j].id+"\"";
				appendTo = appendTo +">"+categoryJSON.categories[j].id+" (";
				appendTo = appendTo +categoryJSON.categories[j].label+")</option>";	
				
			}
			appendType += "<option value=\"Sub\"";
			appendType += ">Sub</option>";
			appendType += "<option value=\"Equality\"";
			appendType += ">Equality</option>";
			$("#fromId").empty();
			$("#toId").empty();
			$("#id").empty();
			$("#type").empty();
			$("#id").val(relId);
			$("#fromId").append(appendFrom);
			$("#toId").append(appendTo);
			$("#type").val(json.type);
		})
	});
}

function updateRelationship(form){
	var json = "{\"id\":"+form[0].value;
	json = json + ", \"fromId\":"+form[1].value;
	json = json + ", \"toId\":"+form[2].value;
	json = json + ", \"type\": \""+form[3].value+"\"";
	json = json + "}";
	console.log(json);
	
	var url="../relationships/"+form[0].value;
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'PUT',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	});
}

function deleteRelationship(relId){
	var url="../relationships/"+relId;
	var json="";

	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	});
}

/**
 * 	Targetfunction functions
 */

function findRootId(categories, assignments, roots){
	var rootId=categories[0].id;
	var maxImplicit=0;
	for(var i=0;i<categories.length;i++){
		var implicits=0;
		if(assignments!=null){
			for(var j=0; j<assignments.length; j++){
				if(assignments[j].categoryId==categories[i].id&&assignments[j].id==-1){
					implicits++;
				}
			}
		}	
		if(implicits>maxImplicit&&!roots.includes(categories[i].id)){
			maxImplicit=implicits;
			rootId = categories[i].id;
		}
		
	}
	return rootId;
}

function getCategoryAssignmentArray(assignments, catId){
	var output = new Array();
	if(assignments!=null){
		for(var i=0;i<assignments.length;i++){
			if(assignments[i].categoryId==catId){
				output.push(assignments[i]);
			}
		}
	}
	console.log("category "+catId+" has "+output.length+" assignments: ");
	console.log(output);
	return output;
}

function hideDesc(catId){
	$("#cat"+catId+"desc").hide();
	$("#cat"+catId+"hideCats").hide();
	$("#cat"+catId+"showCats").show();
}

function showDesc(catId){
	$("#cat"+catId+"desc").show();
	$("#cat"+catId+"showCats").hide();
	$("#cat"+catId+"hideCats").show();
}

function hideDocs(catId){
	$("#cat"+catId+"docs").hide();
	$("#cat"+catId+"hideDocs").hide();
	$("#cat"+catId+"showDocs").show();
}

function showDocs(catId){
	$("#cat"+catId+"docs").show();
	$("#cat"+catId+"showDocs").hide();
	$("#cat"+catId+"hideDocs").show();
	
}

function showEq(catId){
	$.getJSON("../relationships/ancestors/"+catId,function(json){
		for(var i=json.categories.length-1; i>=0; i--){
			showDesc(json.categories[i].id);
		}
	});
}

function getDescendantAmount(relationships, catId){
	console.log(relationships);
	console.log(catId);
	var descendants=0;
	for(var j=0; j<relationships.length; j++){
		if(relationships[j].type=="Sub"&&relationships[j].fromId==catId){
			descendants++;
		}
	}	
	return descendants;
}



function generateTargetFunctionCategory(categories, descendants, equalities, assignments, documents, i, visited, depth){
	var appendString="";
	if(!visited.includes(categories[i])){
		visited.push(categories[i]);
		appendString = "<div id=\"cat"+categories[i].id+"\" style=\"padding-left: 10px\">";
		appendString += "<table>";
		appendString += "<tr><td>";	
		appendString += "<b>Category "+categories[i].label;
		appendString += "</b> <a href=\"assignment.html?catId="+categories[i].id+"\" class=\"button next\">Assign document</a></td></tr>";
		if(typeof equalities[i]!="undefined" && equalities[i].length>0){
				appendString += " equal to ";
			for(var j=0;j<equalities[i].length;j++){
				var eq = getObjectById(categories, equalities[i][j]);
				appendString += "<a href=\"#cat"+eq.id+"\" onclick=\"showEq("+eq.id+")\">"
				appendString += eq.label+"</a>";
				if(j<equalities[i].length-1){
					appendString += ", ";
				}
					
			}
		}		
		appendString += "</b></td></tr>";
		/*
		appendString += "<tr><td><label>"
		appendString += assignments[i].length+" assigned documents </label></td><td>";
		appendString += "<button onclick=\"hideDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideDocs\">Hide</button>";
		appendString += "<button onclick=\"showDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"showDocs\">Show</button>";
		appendString += "<td"
		if(typeof descendants[i]!="undefined" && descendants[i].length>0){
			appendString += "><label>"+descendants[i].length+" sub-categories</label></td><td>";
			appendString += "<button onclick=\"hideDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideCats\">Hide</button>";
			appendString += "<button onclick=\"showDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"showCats\">Show</button>";
		}else{
			appendString += "colspan=2>"
		}
		appendString += "</td></tr>";
		*/
		
		appendString += "<tr><td><b>"
		appendString += categories[i].label+": ";
		appendString += assignments[i].length+" assigned documents </b> ";
		appendString += "<button onclick=\"hideDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideDocs\">Hide</button>";
		appendString += "<button onclick=\"showDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"showDocs\">Show</button>";
		appendString += "<br/>";	
		if(assignments[i].length>0){
			appendString += "<div id=\"cat"+categories[i].id+"docs\">";
			appendString += "<table>";
			for(var j=0; j<assignments[i].length; j++){
				appendString += "<tr><td>"
				var document = getObjectById(documents,assignments[i][j].documentId);
				if(assignments[i][j].id==-1){
					appendString += "Implicit: ";
				}else{
					appendString += "Direct: ";
				}
				appendString += "</td><td><a href=\"document.html?docId="+document.id+"\">"
				appendString += document.label+"</a></td><td>"
				appendString += "<a href=\"assignment.html?catId="+categories[i].id+"&docId="+document.id+"&assId="+assignments[i][j].id+"\" class=\"button next\">Edit assignment</a>";
				appendString += "</td></tr>";
			}
			appendString += "</table>";
			appendString += "</div>";
		}
		appendString += "</td></tr>"
		
		if(typeof descendants[i]!="undefined"&&descendants[i].length>0){
			appendString += "<tr><td>"
			appendString += "<b>"+categories[i].label+": "+descendants[i].length+" sub-categories</b> ";
			appendString += "<button onclick=\"hideDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideCats\">Hide</button>";
			appendString += "<button onclick=\"showDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"showCats\">Show</button>";
			appendString += "<br/>";
				
			appendString += "<div id=\"cat"+categories[i].id+"desc\">";		
			for(var j=0; j<descendants[i].length; j++){
				console.log("category "+i+" "+categories[i].label+" descendant "+j+" is category: ")
				console.log(categories.indexOf(descendants[i][j]));
				var descIndex = categories.indexOf(descendants[i][j]); 
				var catResult=generateTargetFunctionCategory(categories, descendants, equalities, assignments, documents,descIndex,visited, depth+1);
				appendString += catResult.appendString;
				visited = catResult.visited;
			}
			appendString += "</div>";
			appendString += "</td></tr>"
		}
		appendString += "</div>";
		appendString += "</table>";
	}
	var output = {appendString: appendString, visited: visited};
	return output;
	
	
}

function renderTargetFunction(categoryDisplay, categoryDescendants, categoryEqualities, categoryAssignments, documents){
	$("#list").empty();
	$("#list").append("<h2>Available assignments:</h2>");
	if(categoryDisplay.length==0){
		$("#list").append("<h3>There is currently no target function in this microservice. You can add one</h3>");
	}else{
		var visited = new Array();
		for(var i=0;i<categoryDisplay.length;i++){
			
			var catResult = generateTargetFunctionCategory(categoryDisplay,categoryDescendants,categoryEqualities, categoryAssignments, documents, i, visited, 0);
			var appendString = catResult.appendString;
			visited = catResult.visited;
			console.log("rendering category");
			console.log(visited);
			
			$("#list").append(appendString);
			$("#cat"+categoryDisplay[i].id+"desc").hide();
			$("#cat"+categoryDisplay[i].id+"hideCats").hide();
			$("#cat"+categoryDisplay[i].id+"docs").hide();
			$("#cat"+categoryDisplay[i].id+"hideDocs").hide();
		}
	}
	
}

function readTargetFunction(){
	console.log("Reading the target function");
	var categoryJSON;
	var documentJSON;
	var relationshipJSON;
	var targetfunctionJSON;
	
	$.getJSON("../categories",function(json){
		categoryJSON = json;
	}).done(function(){
		$.getJSON("../documents",function(json){
			documentJSON = json;
		}).done(function(){
				$.getJSON("../relationships",function(json){
				relationshipJSON = json;
			}).done(function(){
				$.getJSON("../targetfunction",function(json){
					targetfunctionJSON = json;
				}).done(function(){
					
					var categoryDisplay = new Array();
					var categoryDescendants = new Array();
					var categoryEqualities = new Array();
					var categoryAssignments = new Array();
					
					var categoryDisplayCounter = 0;
					if(relationshipJSON.relationships==null){
						for(var i=0;i<categoryJSON.categories.length;i++){
							categoryDisplay[categoryDisplayCounter]=categoryJSON.categories[i];
							categoryAssignments[categoryDisplayCounter]=getCategoryAssignmentArray(targetfunctionJSON.assignments,categoryJSON.categories[i].id);
							categoryDisplayCounter++;
						}
					}else{
						var roots = new Array();
									
						var rootId=findRootId(categoryJSON.categories, targetfunctionJSON.assignments, roots);
						//implement BFS to find everything connected to the root. Repeat for all remaining nodes.
						var visited = new Array();
						var queue = new Array();
						
						var unvisited = getIds(categoryJSON.categories);
						//visited.push(rootId);
						queue.push(rootId);
						roots.push(rootId);
						while(queue.length>0){
							var catId=queue.pop();
							visited.push(catId);
							unvisited=deleteFromArray(unvisited, catId);
							console.log("Processing category "+catId);
							console.log(visited);	
							console.log(unvisited)
							for(var j=0;j<categoryJSON.categories.length;j++){
								if(categoryJSON.categories[j].id==catId){
									categoryDisplay[categoryDisplayCounter]=categoryJSON.categories[j];
								}
							}
							categoryDescendants[categoryDisplayCounter]=new Array();
							categoryEqualities[categoryDisplayCounter]=new Array();
							for (var i=0;i<relationshipJSON.relationships.length;i++){
								var rel=relationshipJSON.relationships[i];
								if(rel.type=="Sub"&&rel.fromId==catId&&!visited.includes(rel.toId)){
									queue.push(rel.toId)
									for(var j=0;j<categoryJSON.categories.length;j++){
										var cat=categoryJSON.categories[j];
										if(cat.id==rel.toId){
											categoryDescendants[categoryDisplayCounter].push(cat);
										}
									}							
								}else if(rel.type=="Equality"&&rel.fromId==catId){
									categoryEqualities[categoryDisplayCounter].push(rel.toId);
								}else if(rel.type=="Equality"&&rel.toId==catId){
									categoryEqualities[categoryDisplayCounter].push(rel.fromId);
								}
							}
							categoryAssignments[categoryDisplayCounter]=getCategoryAssignmentArray(targetfunctionJSON.assignments,catId);
							categoryDisplayCounter++;
							if(queue.length==0&&unvisited.length>0){
								//check for alternative roots
								rootId=findRootId(buildArrayFromIds(categoryJSON.categories,unvisited), targetfunctionJSON.assignments, roots);
								console.log("Found new root: "+rootId);
								console.log("Has descendants: "+getDescendantAmount(relationshipJSON.relationships,rootId));
								if(!roots.includes(rootId)&&getDescendantAmount(relationshipJSON.relationships,rootId)>0){
									console.log("pushing rootId: "+rootId);
									queue.push(rootId);
								}else{
									console.log("unvisited: "+unvisited);
									for(var i=0; i<unvisited.length; i++){
										categoryDisplay[categoryDisplayCounter]=getObjectById(categoryJSON.categories, unvisited[i]);
										categoryAssignments[categoryDisplayCounter]=getCategoryAssignmentArray(targetfunctionJSON.assignments,unvisited[i]);
										categoryDisplayCounter++;
									}
								}										
							}
						}
						
						
					}
					
					console.log(categoryDisplay);
					console.log(categoryDescendants);
					console.log(categoryEqualities);
					console.log(categoryAssignments);
					renderTargetFunction(categoryDisplay, categoryDescendants, categoryEqualities, categoryAssignments, documentJSON.documents);
					
				});
			});
			
		});
		
	});
}

function renderAssignmentCreateForm(){
	$("#docId").empty();
	$("#catId").empty();
	var categoryJSON;
	var documentJSON;
	$.getJSON("../categories",function(json){
		categoryJSON = json;
	}).done(function(){
		$.getJSON("../documents",function(json){
			documentJSON = json;
		}).done(function(){
			var appendString = "";			
			for(var j=0; j< documentJSON.documents.length; j++){				
				appendString = appendString +"<option value=\""+documentJSON.documents[j].id+"\">";
				appendString = appendString +documentJSON.documents[j].id+" (";
				appendString = appendString +documentJSON.documents[j].label+")</option>";				
			}
			$("#docId").append(appendString);
						
			appendString = "";			
			for(var j=0; j< categoryJSON.categories.length; j++){			
				appendString = appendString +"<option value=\""+categoryJSON.categories[j].id+"\">";
				appendString = appendString +categoryJSON.categories[j].id+" (";
				appendString = appendString +categoryJSON.categories[j].label+")</option>";			
			}
			$("#catId").append(appendString);			
		})
	});
}

function createAssignment(form){
	var json = "{ \"assignments\":[{\"id\":"+form[0].value;
	json = json + ", \"documentId\":"+form[1].value;
	json = json + ", \"categoryId\":"+form[2].value+"";
	json = json + "}]}";
	var url="../targetfunction";	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	});
}

function uploadTargetfunctionJSON(json){
	var url="../targetfunction";	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	 });
}

function deleteTargetfunction(){
	var url="../targetfunction";
	var json="";

	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	 });
}

function renderAssignment(assId){
	var appendString="";
	var ass;
	var document;
	var category;
	$.getJSON("../targetfunction/"+assId,function(json){
		ass=json;
	}).done(function(){		
		console.log(ass);
		console.log("../documents/"+ass.documentId);
		console.log("../categories/"+ass.categoryId);
		appendString+="<h3>Assignment "+assId+": ";
		$.getJSON("../documents/"+ass.documentId,function(json){
			console.log(json);
			document=json;
			appendString+="Document "+document.id+" ("+document.label+") is assigned to ";
		}).done(function(){
			$.getJSON("../categories/"+ass.categoryId,function(json){
				console.log(json);
				category=json;
				appendString+="category "+category.id+" ("+category.label+")</h3>";
			}).done(function(){
				$("#list").empty();
				$("#list").append(appendString);
				$("#list").show();
			});
		});		
	}).fail(function(){
		
		appendString+="<h3>An assignment with ID "+assId+" does not exist. You can create it.</h3>";
		$("#list").empty();
		$("#list").append(appendString);
		$("#list").show();
	});
	
}

function renderAssignmentCreateForm(ids){
	$("#docId").empty();
	$("#catId").empty();
	var categoryJSON;
	var documentJSON;
	$.getJSON("../categories",function(json){
		categoryJSON = json;
		console.log(categoryJSON);
	}).done(function(){
		$.getJSON("../documents",function(json){
			documentJSON = json;
			console.log(documentJSON);
		}).done(function(){
			var appendString = "";			
			for(var j=0; j< documentJSON.documents.length; j++){				
				appendString = appendString +"<option value=\""+documentJSON.documents[j].id+"\"";
				if(documentJSON.documents[j].id==ids.docId){
					appendString+= " selected";
				}
				appendString+=">";
				appendString = appendString +documentJSON.documents[j].id+" (";
				appendString = appendString +documentJSON.documents[j].label+")</option>";				
			}
			$("#docId").append(appendString);
						
			appendString = "";			
			for(var j=0; j< categoryJSON.categories.length; j++){			
				appendString = appendString +"<option value=\""+categoryJSON.categories[j].id+"\"";
				if(categoryJSON.categories[j].id==ids.catId){
					appendString+= " selected";
				}
				appendString+=">";
				appendString = appendString +categoryJSON.categories[j].id+" (";
				appendString = appendString +categoryJSON.categories[j].label+")</option>";			
			}
			$("#catId").append(appendString);
			console.log("attempting id Setting: "+ids.assId);
			if(idIsSet(ids.assId)){
				console.log("Id is set");
				$("#id").val(ids.assId);
			}
			
		})
	});
}

function updateAssignment(form){
	var json = "{\"id\":"+form[0].value+
		",\"documentId\":\""+form[1].value;
		json = json+"\",\"categoryId\":\"";
		json = json+form[2].value+"\"}";
		console.log(json);

	var url="../targetfunction/"+form[0].value;
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'PUT',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	});
}

function deleteAssignment(assId){
	var json="";
	var url="../targetfunction/"+assId;	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	});
}

/**
 * Configuration Functions. These are overwritten for each different classifier trainer
*/

function renderConfigurations(){
$("#list").empty();
$("#list").append("<h2>Available configurations:</h2>");
$.getJSON("../configurations",function(json){	
	if(json.configurations==null){
		$("#list").append("<h3>There are currently no configurations in this microservice. You can add one</h3>");
	}else{
		for (var i=0; i< json.configurations.length; i++){
			$("#list").append("<li><a href=\"configuration.html?confId="+json.configurations[i].id+"\">/configurations/"+json.configurations[i].id+"</a></li>");
		}
	}
});
}

function renderConfiguration(confId){
$("#list").empty();
$("#list").append("<h2>Available configuration:</h2>");
$.getJSON("../configurations/"+confId,function(json){
	if(json==null){
		$("#list").append("<h3>The configuration with id "+confId+" does not exist. You can create it</h3>");
	}else{
		$("#list").append("<h3>Id: "+json.id+"</h3><ul>");
		$("#list").append("<li>Folds: "+json.folds+"</li>");
		$("#list").append("<li>Include Implicits: "+json.includeImplicits+"</li>");
		$("#list").append("<li>Assignment Threshold: "+json.assignmentThreshold+"</li>");
		$("#list").append("<li>Selection Policy: "+json.selectionPolicy+"</li>");
		$("#list").append("<li>Top Terms per Category: "+json.topTermsPerCat+"</li>");
		$("#list").append("<li>SVM Parameters: "+JSON.stringify(json.svmParameter)+"</li>");
		$("#list").append("</ul>");
	}
});
}

function createConfiguration(form){
	var json = "{ \"configurations\":[{\"id\":"+form[0].value+
				", \"folds\":"+form[1].value+
				", \"includeImplicits\":"+form[2].value+
				", \"assignmentThreshold\":"+form[3].value+
				", \"selectionPolicy\": \""+form[4].value+"\""+
				", \"topTermsPerCat\":"+form[5].value+
				", \"svmParameter\": {"+
				"  \"svm_type\": \""+form[6].value+"\""+
				", \"kernel_type\": \""+form[7].value+"\""+
				", \"degree\":"+form[8].value+
				", \"gamma\":"+form[9].value+
				", \"coef0\":"+form[10].value+
				", \"eps\":"+form[11].value+
				", \"nr_weight\": 0, \"weight_label\": null, \"weight\": null"+
				", \"c\":"+form[12].value+
				", \"nu\":"+form[13].value+
				", \"p\":"+form[14].value+
				", \"shrinking\":"+form[15].value+
				", \"probability_estimates\":"+form[16].value+
				" }}]}";
	console.log(json);
	
	var url="../configurations";
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function updateConfiguration(form){
	var json = "{ \"id\":"+form[0].value+
				", \"folds\":"+form[1].value+
				", \"includeImplicits\":"+form[2].value+
				", \"assignmentThreshold\":"+form[3].value+
				", \"selectionPolicy\": \""+form[4].value+"\""+
				", \"topTermsPerCat\":"+form[5].value+
				", \"svmParameter\": {"+
				"  \"svm_type\": \""+form[6].value+"\""+
				", \"kernel_type\": \""+form[7].value+"\""+
				", \"degree\":"+form[8].value+
				", \"gamma\":"+form[9].value+
				", \"coef0\":"+form[10].value+
				", \"eps\":"+form[11].value+
				", \"nr_weight\": 0, \"weight_label\": null, \"weight\": null"+
				", \"c\":"+form[12].value+
				", \"nu\":"+form[13].value+
				", \"p\":"+form[14].value+
				", \"shrinking\":"+form[15].value+
				", \"probability_estimates\":"+form[16].value+
				"} }";
	console.log(json);
	
	var url="../configurations/"+form[0].value;
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'PUT',
	    dataType: 'json',
	    data: json,
	    success: function(data){
			 console.log('succes: '+data);
		}
	 });
}

function renderConfigurationUpdate(confId){
	$.getJSON("../configurations/"+confId,function(json){
		if(json==null){
			//nothing but empty fields
		}else{
			$("#id").val(json.id);
			$("#folds").val(json.folds);
			$("#includeImplicits").empty();
			if(json.includeImplicits==true){
				$("#includeImplicits").append("<option value=\"true\" selected>true</selected>");
				$("#includeImplicits").append("<option value=\"false\">false</selected>");
			}else{
				$("#includeImplicits").append("<option value=\"true\">true</selected>");
				$("#includeImplicits").append("<option value=\"false\" selected>false</selected>");
			}
			$("#assignmentThreshold").val(json.assignmentThreshold);
			$("#selectionPolicy").empty();
			if(json.selectionPolicy=="MicroaverageF1"){
				$("#selectionPolicy").append("<option value=\"MicroaverageF1\" selected>Microaverage F1</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MicroaverageF1\">Microaverage F1</option>");
			}
			if(json.selectionPolicy=="MicroaveragePrecision"){
				$("#selectionPolicy").append("<option value=\"MicroaveragePrecision\" selected>Microaverage Precision</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MicroaveragePrecision\">Microaverage Precision</option>");
			}
			if(json.selectionPolicy=="MicroaverageRecall"){
				$("#selectionPolicy").append("<option value=\"MicroaverageRecall\" selected>Microaverage Recall</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MicroaverageRecall\">Microaverage Recall</option>");
			}
			if(json.selectionPolicy=="MacroaverageF1"){
				$("#selectionPolicy").append("<option value=\"MacroaverageF1\" selected>Macroaverage F1</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MacroaverageF1\">Macroaverage F1</option>");
			}
			if(json.selectionPolicy=="MacroaveragePrecision"){
				$("#selectionPolicy").append("<option value=\"MacroaveragePrecision\" selected>Macroaverage Precision</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MacroaveragePrecision\">Macroaverage Precision</option>");
			}
			if(json.selectionPolicy=="MacroaverageRecall"){
				$("#selectionPolicy").append("<option value=\"MacroaverageRecall\" selected>Macroaverage Recall</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MacroaverageRecall\">Macroaverage Recall</option>");
			}
			$("#topTermsPerCat").val(json.topTermsPerCat);
			$("#svm_type").empty
			if(json.svmParameter.svm_type=="c_svc"){
				$("#svm_type").append("<option value=\"c_svc\" selected>c_svc</option>");
				$("#svm_type").append("<option value=\"nu_svc\">nu_svc</option>");
			}else{
				$("#svm_type").append("<option value=\"c_svc\">c_svc</option>");
				$("#svm_type").append("<option value=\"nu_svc\" selected>nu_svc</option>");
			}
			$("#kernel_type").empty();
			if(json.svmParameter.kernel_type=="linear"){
				$("#kernel_type").append("<option value=\"linear\" selected>Linear</option>");
				$("#kernel_type").append("<option value=\"polynomial\">Polynomial</option>");
				$("#kernel_type").append("<option value=\"rbf\">RBF</option>");
				$("#kernel_type").append("<option value=\"sigmoid\">Sigmoid</option>");
				$("#kernel_type").append("<option value=\"precomputed\">Precomputed</option>");
			}else if(json.svmParameter.kernel_type=="polynomial"){
				$("#kernel_type").append("<option value=\"linear\">Linear</option>");
				$("#kernel_type").append("<option value=\"polynomial\" selected>Polynomial</option>");
				$("#kernel_type").append("<option value=\"rbf\">RBF</option>");
				$("#kernel_type").append("<option value=\"sigmoid\">Sigmoid</option>");
				$("#kernel_type").append("<option value=\"precomputed\">Precomputed</option>");
			}else if(json.svmParameter.kernel_type=="rbf"){
				$("#kernel_type").append("<option value=\"linear\">Linear</option>");
				$("#kernel_type").append("<option value=\"polynomial\">Polynomial</option>");
				$("#kernel_type").append("<option value=\"rbf\" selected>RBF</option>");
				$("#kernel_type").append("<option value=\"sigmoid\">Sigmoid</option>");
				$("#kernel_type").append("<option value=\"precomputed\">Precomputed</option>");
			}else if(json.svmParameter.kernel_type=="sigmoid"){
				$("#kernel_type").append("<option value=\"linear\">Linear</option>");
				$("#kernel_type").append("<option value=\"polynomial\">Polynomial</option>");
				$("#kernel_type").append("<option value=\"rbf\">RBF</option>");
				$("#kernel_type").append("<option value=\"sigmoid\" selected>Sigmoid</option>");
				$("#kernel_type").append("<option value=\"precomputed\">Precomputed</option>");
			}else if(json.svmParameter.kernel_type=="precomputed"){
				$("#kernel_type").append("<option value=\"linear\">Linear</option>");
				$("#kernel_type").append("<option value=\"polynomial\">Polynomial</option>");
				$("#kernel_type").append("<option value=\"rbf\">RBF</option>");
				$("#kernel_type").append("<option value=\"sigmoid\">Sigmoid</option>");
				$("#kernel_type").append("<option value=\"precomputed\" precomputed>Precomputed</option>");
			}
			$("#degree").val(json.svmParameter.degree);
			$("#gamma").val(json.svmParameter.gamma);
			$("#coef0").val(json.svmParameter.coef0);
			$("#eps").val(json.svmParameter.eps);
			$("#c").val(json.svmParameter.c);
			$("#nu").val(json.svmParameter.nu);
			$("#p").val(json.svmParameter.p);
			$("#shrinking").empty();
			if(json.svmParameter.shrinking=="0"){
				$("#shrinking").append("<option value=\"0\" selected>0: Don't use shrinking heuristic</option>");
				$("#shrinking").append("<option value=\"1\">1: Use shrinking heuristic</option>");
			}else{
				$("#shrinking").append("<option value=\"0\">0: Don't use shrinking heuristic</option>");
				$("#shrinking").append("<option value=\"1\" selected>1: Use shrinking heuristic</option>");
			}
			$("#probability_estimates").empty();
			if(json.svmParameter.probability_estimates=="0"){
				$("#probability_estimates").append("<option value=\"0\" selected>0: Don't train for probability estimates</option>");
				$("#probability_estimates").append("<option value=\"1\">1: Train for probability estimates</option>");
			}else{
				$("#probability_estimates").append("<option value=\"0\">0: Don't train for probability estimates</option>");
				$("#probability_estimates").append("<option value=\"1\" selected>1: Train for probability estimates</option>");
			}
		}
	});
}

function uploadConfigurationJSON(json){
	var url="../configurations";	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function deleteAllConfigurations(){
	var url="../configurations";	
	var json="";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function deleteConfiguration(confId){
var url="../configurations/"+confId;
var json="";

	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
			 	console.log('succes: '+data);
		}
	 });
}

function renderModels(){
	$("#list").empty();
	$("#list").append("<h2>Available models:</h2>");
	$.getJSON("../models",function(json){	
		if(json.models==null){
			$("#list").append("<h3>There are currently no model in this microservice. You can add one</h3>");
		}else{
			for (var i=0; i< json.models.length; i++){
				
				$("#list").append("<li><a href=\"model.html?modId="+json.models[i].id+"\">/models/"+json.models[i].id+"</a> - " +
						"<a href=\"evaluation.html?modId="+json.models[i].id+"\">/evaluations/"+json.models[i].id+
						"</a> | Progress: "+json.models[i].progress+"</li>");
				
			}
		}
	});

}

function renderConfigurationSelectionForm(){
	$("#configuration").empty();
	var configurationJSON;
	$.getJSON("../configurations",function(json){
		configurationJSON = json;
		console.log(configurationJSON);
	}).done(function(){
		
		var appendString = "";			
		for(var j=0; j< configurationJSON.configurations.length; j++){				
			appendString = appendString +"<option value=\""+configurationJSON.configurations[j].id+"\"";
			appendString+=">";
			appendString = appendString +configurationJSON.configurations[j].id+" (";
			appendString = appendString +configurationJSON.configurations[j].folds+" folds, includeImplicits: ";
			appendString = appendString +configurationJSON.configurations[j].includeImplicits+", ";
			appendString = appendString +configurationJSON.configurations[j].assignmentThreshold+" assignmentThreshold, ";
			appendString = appendString +configurationJSON.configurations[j].selectionPolicy+" selectionPolicy ";
			appendString = appendString +")</option>";				
		}
		$("#configuration").append(appendString);
		
	});
}

function deleteAllModels(){
	var url="../models";	
	var json="";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function startTraining(confId){
	var url="../models?confId="+confId;	
	var json="";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function renderModel(modId){
	$("#list").empty();
	$("#list").append("<h2>Available model:</h2>");
	$.getJSON("../models/"+modId,function(json){	
	
		$("#list").append("Id: "+json.id+"<br/>");
		$("#list").append("Configuration Id: "+json.configurationId+"<br/>");
		$("#list").append("Include implicits: "+json.configuration.includeImplicits+"<br/>");
		$("#list").append("Progress:"+json.progress+"<br/>");
		$("#list").append("TrainingLog:<br/> "+json.trainingLog+"<br/>");
		$("#list").append("<h4>Configuration:</h4>");
		$("#list").append("Id:"+json.configuration.id+"<br/>");
		$("#list").append("Folds:"+json.configuration.folds+"<br/>");
		$("#list").append("IncludeImplicits:"+json.configuration.includeImplicits+"<br/>");
		$("#list").append("SelectionPolicy:"+json.configuration.selectionPolicy+"<br/>");
		$("#list").append("TopTermsPerCat:"+json.configuration.topTermsPerCat+"<br/>");
		$("#list").append("<h4>Controlled Vocabulary:</h4>");
		for(var i=0;i<json.controlledVocabulary.length; i++){
			$("#list").append("id:"+json.controlledVocabulary[i].id+" term: "+json.controlledVocabulary[i].term+" documentFrequency: "+
					json.controlledVocabulary[i].documentFrequency+" sumDimensionsSquares: "+json.controlledVocabulary[i].sumDimensionSquares+"<br/>");
		}
		$("#list").append("SvmModel:"+JSON.stringify(json.svmModel)+"<br/>");
		
	}).fail(function(){
		$("#list").empty();
		$("#list").append("<h3>There are currently no such model in this microservice. You can add one by starting a training process</h3>");
	});
}

function deleteModel(modId){
	var url="../models/"+modId;
	var json="";

		$.ajax({
			url: url,
			headers: {
			    'Accept': 'application/json',
		        'Content-Type':'application/json'
		    },
		    method: 'DELETE',
		    dataType: 'json',
		    data: json,
		    success: function(data){
				 	console.log('succes: '+data);
			}
		 });
 }

function renderEvaluations(){
	$("#list").empty();
	$("#list").append("<h2>Available evaluations:</h2>");
	$.getJSON("../evaluations",function(json){	
		if(json.trainingSessions==null){
			$("#list").append("<h3>There are currently no models to be evaluated in this microservice. You can create a new model.</h3>");
		}else{
			for (var i=0; i< json.trainingSessions.length; i++){
				$("#list").append("<li><a href=\"evaluation.html?modId="+json.trainingSessions[i].id+"\">/evaluations/"+json.trainingSessions[i].id+"</a></li>");
			}
		}
	});

}

function renderTrainingSession(modId){
	$.getJSON("../evaluations/"+modId,function(json){	
		if(json.foldEvaluations==null){
			$("#list").append("<h3>There is currently no evaluation for this model available.</h3>");
		}else{
			var appendString ="<ul>";
			appendString+="<li>id: "+json.id+"</li>";
			appendString+="<li>timestamp: "+json.timestamp+"</li>";
			appendString+="<li>description: "+json.description+"</li>";
			appendString+="<li>model id: "+json.modelId+"</li>"
			appendString+="</ul>";
			appendString+="<div id=\"foldEvaluations\">";
			for (var i=0; i< json.foldEvaluations.length; i++){
				appendString+="<div id=\"foldId="+json.foldEvaluations[i].foldId+"\">";
				appendString+="<table>";
				appendString+="<tr><td>Fold: </td><td>"+json.foldEvaluations[i].foldId+"</td>";
				appendString+="<td>Assignment Threshold: </td><td>"+json.foldEvaluations[i].assignmentThreshold+"</td>";
				appendString+="<td>Include Implicits: </td><td>"+json.foldEvaluations[i].includeImplicits+"</td></tr>";
				appendString+="<tr><td>Microaverage Precision: </td><td>"+json.foldEvaluations[i].microaveragePrecision+"</td>";
				appendString+="<td>Microaverage Recall: </td><td>"+json.foldEvaluations[i].microaverageRecall+"</td>";
				appendString+="<td>Microaverage F1: </td><td>"+json.foldEvaluations[i].microaverageF1+"</td></tr>";
				appendString+="<tr><td>Macroaverage Precision: </td><td>"+json.foldEvaluations[i].macroaveragePrecision+"</td>";
				appendString+="<td>Macroaverage Recall: </td><td>"+json.foldEvaluations[i].macroaverageRecall+"</td>";
				appendString+="<td>Macroaverage F1: </td><td>"+json.foldEvaluations[i].macroaverageF1+"</td></tr>";
				appendString+="</table>";
				appendString+="<div id=\"foldId="+json.foldEvaluations[i].foldId+"Cats\">";
				appendString+="<table>";
				appendString+="<tr><td>Category Id: </td><td>Category Label: </td>";
				appendString+="<td>Category description: </td><td>True Positives</td>";
				appendString+="<td>False Positives</td><td>False Negatives</td>";
				appendString+="<td>Precision</td><td>Recall</td><td>F1</td></tr>";
				for(var j=0; j<json.foldEvaluations[i].categories.length;j++){
					var cat=json.foldEvaluations[i].categories[j];
					appendString+="<tr><td>"+cat.id+"</td><td>"+cat.label+"</td>";
					appendString+="<td>"+cat.description+"</td><td>"+cat.tp+"</td>";
					appendString+="<td>"+cat.fp+"</td><td>"+cat.fn+"</td>";
					appendString+="<td>"+cat.precision+"</td><td>"+cat.recall+"</td><td>"+cat.f1+"</td></tr>";
					
					
				}
				appendString+="</table>";
				appendString+="</div>";
				appendString+="</div>";
			}
			appendString+="</div>";
			$("#list").append(appendString);
		}
	});
	
}

function renderActiveModel(){
	$("#list").empty();
	$("#list").append("<h2>Active model:</h2>");
	$.getJSON("../model",function(json){	
	
		$("#list").append("Id: "+json.id+"<br/>");
		$("#list").append("Configuration Id: "+json.configurationId+"<br/>");
		$("#list").append("Include implicits: "+json.configuration.includeImplicits+"<br/>");
		$("#list").append("Progress:"+json.progress+"<br/>");
		$("#list").append("TrainingLog:<br/> "+json.trainingLog+"<br/>");
		$("#list").append("<h4>Configuration:</h4>");
		$("#list").append("Id:"+json.configuration.id+"<br/>");
		$("#list").append("Folds:"+json.configuration.folds+"<br/>");
		$("#list").append("IncludeImplicits:"+json.configuration.includeImplicits+"<br/>");
		$("#list").append("SelectionPolicy:"+json.configuration.selectionPolicy+"<br/>");
		$("#list").append("TopTermsPerCat:"+json.configuration.topTermsPerCat+"<br/>");
		$("#list").append("<h4>Controlled Vocabulary:</h4>");
		for(var i=0;i<json.controlledVocabulary.length; i++){
			$("#list").append("id:"+json.controlledVocabulary[i].id+" term: "+json.controlledVocabulary[i].term+" documentFrequency: "+
					json.controlledVocabulary[i].documentFrequency+" sumDimensionsSquares: "+json.controlledVocabulary[i].sumDimensionSquares+"<br/>");
		}
		$("#list").append("SvmModel:"+JSON.stringify(json.svmModel)+"<br/>");
		
	}).fail(function(){
		$("#list").empty();
		$("#list").append("<h3>There is no active model in this microservice. You can assign one.</h3>");
	});
}

function retrieveActiveModel(url){
	var json="";
	var fullUrl = "../model?loadFrom="+url;
		$.ajax({
			url: fullUrl,
			headers: {
			    'Accept': 'application/json',
		        'Content-Type':'application/json'
		    },
		    method: 'POST',
		    dataType: 'json',
		    data: json,
		    success: function(data){
				 	console.log('succes: '+data);
			}
		 });
}

function uploadActiveModel(json){
	var url="../model";	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function generateCategorizationCategory(categories, descendants, equalities, assignments, documents, i, visited, depth){
	var appendString="";
	if(!visited.includes(categories[i])){
		visited.push(categories[i]);
		appendString = "<div id=\"cat"+categories[i].id+"\" style=\"padding-left: 10px\">";
		appendString += "<table>";
		appendString += "<tr><td>";	
		appendString += "<b>Category "+categories[i].label;
		appendString += "</b> <a href=\"assignment.html?catId="+categories[i].id+"\" class=\"button next\">Assign document</a></td></tr>";
		if(typeof equalities[i]!="undefined" && equalities[i].length>0){
				appendString += " equal to ";
			for(var j=0;j<equalities[i].length;j++){
				var eq = getObjectById(categories, equalities[i][j]);
				appendString += "<a href=\"#cat"+eq.id+"\" onclick=\"showEq("+eq.id+")\">"
				appendString += eq.label+"</a>";
				if(j<equalities[i].length-1){
					appendString += ", ";
				}
					
			}
		}		
		appendString += "</b></td></tr>";
		/*
		appendString += "<tr><td><label>"
		appendString += assignments[i].length+" assigned documents </label></td><td>";
		appendString += "<button onclick=\"hideDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideDocs\">Hide</button>";
		appendString += "<button onclick=\"showDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"showDocs\">Show</button>";
		appendString += "<td"
		if(typeof descendants[i]!="undefined" && descendants[i].length>0){
			appendString += "><label>"+descendants[i].length+" sub-categories</label></td><td>";
			appendString += "<button onclick=\"hideDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideCats\">Hide</button>";
			appendString += "<button onclick=\"showDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"showCats\">Show</button>";
		}else{
			appendString += "colspan=2>"
		}
		appendString += "</td></tr>";
		*/
		
		appendString += "<tr><td><b>"
		appendString += categories[i].label+": ";
		appendString += assignments[i].length+" assigned documents </b> ";
		appendString += "<button onclick=\"hideDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideDocs\">Hide</button>";
		appendString += "<button onclick=\"showDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"showDocs\">Show</button>";
		appendString += "<br/>";	
		if(assignments[i].length>0){
			appendString += "<div id=\"cat"+categories[i].id+"docs\">";
			appendString += "<table>";
			for(var j=0; j<assignments[i].length; j++){
				appendString += "<tr><td>"
				var document = getObjectById(documents,assignments[i][j].documentId);
				appendString += "</td><td><a href=\"document.html?docId="+document.id+"\">"
				appendString += document.label+"</a></td><td>"
				appendString += "<a href=\"assignment.html?catId="+categories[i].id+"&docId="+document.id+"&assId=-1\" class=\"button next\">Edit assignment</a>";
				appendString += "</td></tr>";
			}
			appendString += "</table>";
			appendString += "</div>";
		}
		appendString += "</td></tr>"
		
		if(typeof descendants[i]!="undefined"&&descendants[i].length>0){
			appendString += "<tr><td>"
			appendString += "<b>"+categories[i].label+": "+descendants[i].length+" sub-categories</b> ";
			appendString += "<button onclick=\"hideDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideCats\">Hide</button>";
			appendString += "<button onclick=\"showDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"showCats\">Show</button>";
			appendString += "<br/>";
				
			appendString += "<div id=\"cat"+categories[i].id+"desc\">";		
			for(var j=0; j<descendants[i].length; j++){
				console.log("category "+i+" "+categories[i].label+" descendant "+j+" is category: ")
				console.log(categories.indexOf(descendants[i][j]));
				var descIndex = categories.indexOf(descendants[i][j]); 
				var catResult=generateTargetFunctionCategory(categories, descendants, equalities, assignments, documents,descIndex,visited, depth+1);
				appendString += catResult.appendString;
				visited = catResult.visited;
			}
			appendString += "</div>";
			appendString += "</td></tr>"
		}
		appendString += "</div>";
		appendString += "</table>";
	}
	var output = {appendString: appendString, visited: visited};
	return output;
	
	
}

function renderCategorizations(categoryDisplay, categoryDescendants, categoryEqualities, categoryAssignments, documents){
	$("#list").empty();
	$("#list").append("<h2>Available assignments:</h2>");
	if(categoryDisplay.length==0){
		$("#list").append("<h3>There is currently no target function in this microservice. You can add one</h3>");
	}else{
		var visited = new Array();
		for(var i=0;i<categoryDisplay.length;i++){
			
			var catResult = generateCategorizationCategory(categoryDisplay,categoryDescendants,categoryEqualities, categoryAssignments, documents, i, visited, 0);
			var appendString = catResult.appendString;
			visited = catResult.visited;
			console.log("rendering category");
			console.log(visited);
			
			$("#list").append(appendString);
			$("#cat"+categoryDisplay[i].id+"desc").hide();
			$("#cat"+categoryDisplay[i].id+"hideCats").hide();
			$("#cat"+categoryDisplay[i].id+"docs").hide();
			$("#cat"+categoryDisplay[i].id+"hideDocs").hide();
		}
	}
	
}

function readCategorizations(){
	console.log("Reading the target function");
	var categoryJSON;
	var documentJSON;
	var relationshipJSON;
	var categorizationJSON;
	
	$.getJSON("../categories",function(json){
		categoryJSON = json;
	}).done(function(){
		$.getJSON("../documents",function(json){
			documentJSON = json;
		}).done(function(){
				$.getJSON("../relationships",function(json){
				relationshipJSON = json;
			}).done(function(){
				$.getJSON("../categorizations",function(json){
					categorizationJSON = json;
					console.log("Categorizations are: ");
					console.log(categorizationJSON);
				}).done(function(){
					
					var categoryDisplay = new Array();
					var categoryDescendants = new Array();
					var categoryEqualities = new Array();
					var categoryAssignments = new Array();
					
					var categoryDisplayCounter = 0;
					if(relationshipJSON.relationships==null){
						for(var i=0;i<categoryJSON.categories.length;i++){
							categoryDisplay[categoryDisplayCounter]=categoryJSON.categories[i];
							categoryAssignments[categoryDisplayCounter]=getCategoryAssignmentArray(categorizationJSON.categorizations,categoryJSON.categories[i].id);
							categoryDisplayCounter++;
						}
					}else{
						var roots = new Array();
									
						var rootId=findRootId(categoryJSON.categories, categorizationJSON.categorizations, roots);
						//implement BFS to find everything connected to the root. Repeat for all remaining nodes.
						var visited = new Array();
						var queue = new Array();
						
						var unvisited = getIds(categoryJSON.categories);
						//visited.push(rootId);
						queue.push(rootId);
						roots.push(rootId);
						while(queue.length>0){
							var catId=queue.pop();
							visited.push(catId);
							unvisited=deleteFromArray(unvisited, catId);
							console.log("Processing category "+catId);
							console.log(visited);	
							console.log(unvisited)
							for(var j=0;j<categoryJSON.categories.length;j++){
								if(categoryJSON.categories[j].id==catId){
									categoryDisplay[categoryDisplayCounter]=categoryJSON.categories[j];
								}
							}
							categoryDescendants[categoryDisplayCounter]=new Array();
							categoryEqualities[categoryDisplayCounter]=new Array();
							for (var i=0;i<relationshipJSON.relationships.length;i++){
								var rel=relationshipJSON.relationships[i];
								if(rel.type=="Sub"&&rel.fromId==catId&&!visited.includes(rel.toId)){
									queue.push(rel.toId)
									for(var j=0;j<categoryJSON.categories.length;j++){
										var cat=categoryJSON.categories[j];
										if(cat.id==rel.toId){
											categoryDescendants[categoryDisplayCounter].push(cat);
										}
									}							
								}else if(rel.type=="Equality"&&rel.fromId==catId){
									categoryEqualities[categoryDisplayCounter].push(rel.toId);
								}else if(rel.type=="Equality"&&rel.toId==catId){
									categoryEqualities[categoryDisplayCounter].push(rel.fromId);
								}
							}
							categoryAssignments[categoryDisplayCounter]=getCategoryAssignmentArray(categorizationJSON.categorizations,catId);
							categoryDisplayCounter++;
							if(queue.length==0&&unvisited.length>0){
								//check for alternative roots
								rootId=findRootId(buildArrayFromIds(categoryJSON.categories,unvisited), categorizationJSON.categorizations, roots);
								console.log("Found new root: "+rootId);
								console.log("Has descendants: "+getDescendantAmount(relationshipJSON.relationships,rootId));
								if(!roots.includes(rootId)&&getDescendantAmount(relationshipJSON.relationships,rootId)>0){
									console.log("pushing rootId: "+rootId);
									queue.push(rootId);
								}else{
									console.log("unvisited: "+unvisited);
									for(var i=0; i<unvisited.length; i++){
										categoryDisplay[categoryDisplayCounter]=getObjectById(categoryJSON.categories, unvisited[i]);
										categoryAssignments[categoryDisplayCounter]=getCategoryAssignmentArray(categorizationJSON.categorizations,unvisited[i]);
										categoryDisplayCounter++;
									}
								}										
							}
						}
						
						
					}
					
					console.log(categoryDisplay);
					console.log(categoryDescendants);
					console.log(categoryEqualities);
					console.log(categoryAssignments);
					renderCategorizations(categoryDisplay, categoryDescendants, categoryEqualities, categoryAssignments, documentJSON.documents);
					
				});
			});
			
		});
		
	});
}

function deleteCategorizations(){
	var url="../categorizations";
	var json="";

	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
	    	console.log('something worked');
			 	console.log('succes: '+data);
		}
	 });
}

function createCategorization(form){
	var json = "{\"id\":"+form[0].value+
	",\"label\":\""+form[1].value;
	json = json+"\",\"content\":\"";
	json = json+form[2].value.replace(/(\r\n|\n|\r)/gm," ");;
	json = json+"\",\"url\":\""+form[3].value+"\"}";
	console.log(json);
	
	var url="../categorizations";
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function renderDocumentList(){
	$.getJSON("../documents",function(json){	
		if(json.documents!=null){
			for (var i=0; i< json.documents.length; i++){
				$("#docId").append("<option value=\""+json.documents[i].id+"\">"+json.documents[i].id+": "+json.documents[i].label+"</option>");
			}
		}
	});
}

function categorizeExistingDocument(docId){
	var json = "";
	
	var url="../categorizations/existing/"+docId;
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function renderRetraining(){
	$("#list").empty();
	$.getJSON("../retraining",function(json){	
		if(json.needsRetraining){
			$("#list").append("needs retraining!");
		}else{
			$("#list").append("doesn't need retraining.");
		}
	});

}

function renderMetadata(){
	$("#list").empty();
	$.getJSON("../metadata",function(json){	
		$("#list").append("<ul>");
		$("#list").append("<li>algorithm: "+json.algorithm+"</li>");
		$("#list").append("<li>archetype: "+json.archetype+"</li>");
		$("#list").append("<li>configOptions: "+json.configOptions+"</li>");
		$("#list").append("<li>configuration: "+json.configuration+"</li>");
		$("#list").append("<li>debugExamples: "+json.debugExamples+"</li>");
		$("#list").append("<li>Name: "+json.name+"</li>");
		$("#list").append("<li>outputFormat: "+json.outputFormat+"</li>");
		$("#list").append("<li>phases: "+json.phases+"</li>");
		$("#list").append("<li>runType: "+json.runType+"</li>");
		$("#list").append("</ul>");
		$("#list").append("<h3>Calls: </h3>");
		$("#list").append("<ul>");
		for(var i=0; i<json.calls.length; i++){
			$("#list").append("<li>"+json.calls[i]+"</li>");
		}
		$("#list").append("</ul>");
		
		
	});
}