package bbdp.aggregate.exposing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;


public class DoctorAggregateAPI extends HttpServlet {
	public DoctorAggregateAPI() {
		super();
	}	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		String operation = request.getParameter("operation");
		
        DataSource datasource = (DataSource) getServletContext().getAttribute("db");
        Gson gson = new Gson();

		switch(operation) { 
			//get information of the doctor by ID
			case "getDoctorInfoById":{
				String doctorID = request.getParameter("doctorID");
				response.getWriter().write(gson.toJson(DoctorRepo.getDoctorInfoById(datasource,doctorID)));
				break;
			}	
			//get all hospitals
			case "getAllHospitals":{
				response.getWriter().write(gson.toJson(DoctorRepo.getAllHospitals(datasource)));
				break;
			}
			//get all departments by hospital
			case "getDepartmentsByHospital":{
				String hospital = request.getParameter("hospital");
				response.getWriter().write(gson.toJson(DoctorRepo.getDepartmentsByHospital(datasource, hospital)));
				break;
			}
			//get information of the doctors by hospital and department
			case "getDoctorsByHospitalAndDepartment":{
				String hospital = request.getParameter("hospital");
				String department = request.getParameter("department");
				response.getWriter().write(gson.toJson(DoctorRepo.getDoctorsByHospitalAndDepartment(datasource, hospital, department)));
				break;
			}
			
			default:{
				HashMap api = new HashMap();
				api.put("getDoctorInfoById", "get information of the doctor by ID");
				api.put("getAllHospitals", "get all hospitals");
				api.put("getDepartmentsByHospital", "get all departments by hospital");
				api.put("getDoctorsByHospitalAndDepartment", "get information of the doctors by hospital and department");
				response.getWriter().write(gson.toJson(api));
				break;
			}
		}
	}
}

