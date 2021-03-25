package com.example.ex192;

public class StudentInfo {
    private String firstName;
    private String lastName;
    private int stratum;
    private int studClass;
    private VaccineInfo firstVaccine; // if this is null - cant get Vaccine!
    private VaccineInfo secondVaccine;

    public StudentInfo(){}

    public StudentInfo(String firstName, String lastName, int stratum, int studClass, VaccineInfo firstVaccine, VaccineInfo secondVaccine)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.stratum = stratum;
        this.studClass = studClass;
        this.firstVaccine = firstVaccine;
        this.secondVaccine = secondVaccine;
    }

    public StudentInfo(StudentInfo anotherStud)
    {
        this.firstName = anotherStud.firstName;
        this.lastName = anotherStud.lastName;
        this.stratum = anotherStud.stratum;
        this.studClass = anotherStud.studClass;
        this.firstVaccine = anotherStud.firstVaccine;
        this.secondVaccine = anotherStud.secondVaccine;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getStratum()
    {
        return stratum;
    }

    public void setStratum(int stratum) {
        this.stratum = stratum;
    }

    public int getStudClass()
    {
        return studClass;
    }

    public void setStudClass(int studClass) {
        this.studClass = studClass;
    }

    public VaccineInfo getFirstVaccine()
    {
        return firstVaccine;
    }

    public void setFirstVaccine(VaccineInfo firstVaccine) {
        this.firstVaccine = firstVaccine;
    }

    public VaccineInfo getSecondVaccine()
    {
        return secondVaccine;
    }

    public void setSecondVaccine(VaccineInfo secondVaccine) {
        this.secondVaccine = secondVaccine;
    }
}
