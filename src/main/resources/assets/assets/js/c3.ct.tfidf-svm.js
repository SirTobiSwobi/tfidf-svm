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