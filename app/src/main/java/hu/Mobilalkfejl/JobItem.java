package hu.Mobilalkfejl;

public class JobItem {
    private String id;
    private String JobName;
    private String JobDesc;
    private String JobSalary;

    private String JobPhone;



    public JobItem() {}

    public JobItem(String jobName, String jobDesc, String jobSalary, String JobPhone) {
        JobName = jobName;
        JobDesc = jobDesc;
        JobSalary = jobSalary;
        this.JobPhone= JobPhone;
    }

    public String getJobName() {
        return JobName;
    }

    public String getJobDesc() {
        return JobDesc;
    }

    public String getJobSalary() {
        return JobSalary;
    }

    public String _getId() {
        return id;
    }

    public String getJobPhone() {
        return JobPhone;
    }

    public void setJobPhone(String jobPhone) {
        JobPhone = jobPhone;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public void setJobDesc(String jobDesc) {
        JobDesc = jobDesc;
    }

    public void setJobSalary(String jobSalary) {
        JobSalary = jobSalary;
    }
}
