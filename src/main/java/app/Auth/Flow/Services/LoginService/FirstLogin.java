package app.Auth.Flow.Services.LoginService;

import java.util.Scanner;

import app.Auth.Flow.Services.AuthSecurityService.SelectDepartment;
import app.Auth.Flow.Services.AuthSecurityService.SelectJob;
import app.Menus.DepartmentMenu;
import app.Menus.JobMenus.*;
import app.Repository.LoginRepository.HandleAccessManagement;

public class FirstLogin {

    public void FirstSetup(String Username, Scanner scanner) {

        DepartmentMenu show = new DepartmentMenu();
        show.Departments();

        System.out.println("\n[INFO] Please setup an Department");

        SelectDepartment choose = new SelectDepartment();
        choose.Department(scanner);

        int Department = choose.getSelectedDepartment();

        switch (Department) {
            case 1:
                MedicalJobsMenu showMedical = new MedicalJobsMenu();
                showMedical.jobsMenu();
                break;
            case 2:
                EmergencyJobsMenu showEmergency = new EmergencyJobsMenu();
                showEmergency.jobsMenu();
                break;
            case 3:
                LaboratoryJobsMenu showLaboratory = new LaboratoryJobsMenu();
                showLaboratory.jobsMenu();
                break;
            case 4:
                PharmacyJobsMenu showPharmacy = new PharmacyJobsMenu();
                showPharmacy.jobsMenu();
                break;
            case 5:
                itJobsMenu showIT = new itJobsMenu();
                showIT.jobsMenu();
                break;
            case 6:
                SecurityJobsMenu showSecurity = new SecurityJobsMenu();
                showSecurity.jobsMenu();
                break;
            case 7:
                FinanceJobsMenu showFinance = new FinanceJobsMenu();
                showFinance.jobsMenu();
                break;
            case 8:
                OfficeJobsMenu showOffice = new OfficeJobsMenu();
                showOffice.jobsMenu();
                break;
            case 9:
                AdministrationJobsMenu showAdministrative = new AdministrationJobsMenu();
                showAdministrative.jobsMenu();
                break;
            case 10:
                TrainingJobsMenu showTraining = new TrainingJobsMenu();
                showTraining.jobsMenu();
                break;
            case 11:
                SystemJobsMenu showSystem = new SystemJobsMenu();
                showSystem.jobsMenu();
                break;
        }


        HandleAccessManagement run = new HandleAccessManagement();
        run.AccessManagement(Username, Department);
    }
}
