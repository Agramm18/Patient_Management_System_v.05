package app.Auth.Flow.Services.LoginService;

import java.util.Scanner;

import app.Auth.Flow.Services.AuthSecurityService.Management.CollectUserDepartment;
import app.CLIText.Menus.Departments.DepartmentMenu;
import app.CLIText.Menus.DepartmentJobs.*;
import app.Repository.AuthRepository.Management.HasAssignedDepartment;
import app.Repository.AuthRepository.Management.CreateAccessRequest;

import app.Logging.LogManager;
import app.Logging.Enums.ProgrammState.*;

/*
    In this class the first login is handled if the User status is pending in the DB accounts

    Following things will be handled

    1. Setting your Department
    2. Setting your Job
    3. Setting your Role
    4. Setting your Permissions

    If that was set up successfully the DB Will updated your status as active

    The current flow is something like this:

    Show Menu -> Collect User Input as Str. -> Validate Input -> Convert as int -> Validate Input
    Save Request in Access Management

*/

public class FirstLoginFlow {

    public void firstSetup(String Username, Scanner scanner) {

        LogManager.auth(AuthState.INFO, "Starting default User Setup for first login");

        DepartmentMenu show = new DepartmentMenu();
        show.departments();

        LogManager.auth(AuthState.INFO, "Starting Department Selection");

        System.out.println("\n[INFO] Please setup an Department");

        CollectUserDepartment choose = new CollectUserDepartment();
        choose.department(scanner);

        int department = choose.getSelectedDepartment();

        LogManager.auth(AuthState.INFO, "The User have choose the Department: " + department);

        switch (department) {
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

                HasAssignedDepartment validate = new HasAssignedDepartment();
                boolean hasAccess = validate.status(Username);

                if (hasAccess) {
                    showSystem.jobsMenu();
                } else {
                    System.out.println("[ERROR] You don't have the rights to see these Jobs please apply for another job");
                    break;
                }
                break;
        }

        CreateAccessRequest run = new CreateAccessRequest();
        run.accessManagement(Username, department);
    }
}
