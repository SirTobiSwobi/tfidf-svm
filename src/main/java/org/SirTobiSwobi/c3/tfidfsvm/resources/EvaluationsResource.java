package org.SirTobiSwobi.c3.tfidfsvm.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.tfidfsvm.api.TCEvalCategory;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCEvaluation;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCEvaluations;
import org.SirTobiSwobi.c3.tfidfsvm.api.TCTrainingSession;
import org.SirTobiSwobi.c3.tfidfsvm.db.EvalCategory;
import org.SirTobiSwobi.c3.tfidfsvm.db.Evaluation;
import org.SirTobiSwobi.c3.tfidfsvm.db.ReferenceHub;
import org.SirTobiSwobi.c3.tfidfsvm.db.TrainingSession;

@Path("/evaluations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationsResource {
	private ReferenceHub refHub;

	public EvaluationsResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
	public Response getTrainingSessions(@QueryParam("hash") String hash){
		
		TrainingSession[] trainingSessions = refHub.getEvaluationManager().getTrainingSessionArray();
		TCTrainingSession[] TCtrainingSessionArray = new TCTrainingSession[trainingSessions.length];
		
		for(int i=0; i<trainingSessions.length;i++){
			TrainingSession trainingSession = trainingSessions[i];
			TCEvaluation[] foldEvaluations=new TCEvaluation[trainingSession.getEvaluationArray().length];
			Evaluation[] evaluations=trainingSession.getEvaluationArray();
			for(int j=0; j<foldEvaluations.length; j++){
				Evaluation eval = evaluations[j];
				TCEvalCategory[] TCevalCategories = new TCEvalCategory[eval.getEvalCategories().length];
				EvalCategory[] evalCategories = eval.getEvalCategories();
				for(int k=0; k<evalCategories.length; k++){
					EvalCategory evalCat = evalCategories[k];
					TCevalCategories[k]=new TCEvalCategory(evalCat.getId(), evalCat.getLabel(), evalCat.getDescription(), evalCat.getTP(), evalCat.getFP(), evalCat.getFN(),
							evalCat.getPrecision(), evalCat.getRecall(), evalCat.getF1());
				}
				foldEvaluations[j] = new TCEvaluation(eval.isIncludeImplicits(), eval.getAssignmentThreshold(), eval.getMicroaveragePrecision(),
						eval.getMicroaverageRecall(), eval.getMicroaverageF1(), eval.getMacroaveragePrecision(), eval.getMacroaverageRecall(),
						eval.getMacroaverageF1(), eval.getFoldId(), TCevalCategories);
			}
			TCTrainingSession tcTrainingSession = new TCTrainingSession(trainingSession.getModelId(), trainingSession.getTimestamp(), 
							trainingSession.getDescription(), trainingSession.getId(),
			foldEvaluations);
			TCtrainingSessionArray[i]=tcTrainingSession;
		}
		
		TCEvaluations evaluations = new TCEvaluations(TCtrainingSessionArray);
		return Response.ok(evaluations).build();
	}
	
	
	
}
