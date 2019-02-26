package partib.groupProject.probableCauses.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import partib.groupProject.probableCauses.backend.model.bql.query.Estimate;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


public class EstimateOutputCleaner {

	@Autowired
	private static MockMvc mockMvc;

	public static String process(String data, Estimate query) throws Exception {
		String uri = "/util/columnNamesPop/" + query.getPopulation();
		String[] columns = mockMvc.perform(get(uri)).andReturn().getResponse().getContentAsString().replaceAll("[|]", "").split(",");

		String d;

		return null;
	}
}
