package com.example.ex192;

/**
 * The type student info class
 * @author Itey Weintraub <av5350@bs.amalnet.k12.il>
 * @version	1
 * @since 28.3.2021
 * short description:
 *
 *      This class represent a student (that can/not get the vaccine)
 */
public class StudentInfo {
    private String firstName;
    private String lastName;
    private int stratum;
    private int studClass;
    private VaccineInfo firstVaccine; // if this is null - cant get Vaccine!
    private VaccineInfo secondVaccine;

    /**
     * Instantiates a new Student info.
     */
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

    /**
     * Instantiates a new Student info.
     *
     * @param anotherStud the another stud
     */
    public StudentInfo(StudentInfo anotherStud)
    {
        this.firstName = anotherStud.firstName;
        this.lastName = anotherStud.lastName;
        this.stratum = anotherStud.stratum;
        this.studClass = anotherStud.studClass;
        this.firstVaccine = anotherStud.firstVaccine;
        this.secondVaccine = anotherStud.secondVaccine;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Gets stratum.
     *
     * @return the stratum
     */
    public int getStratum()
    {
        return stratum;
    }

    /**
     * Gets stud class.
     *
     * @return the stud class
     */
    public int getStudClass()
    {
        return studClass;
    }

    /**
     * Gets first vaccine.
     *
     * @return the first vaccine
     */
    public VaccineInfo getFirstVaccine()
    {
        return firstVaccine;
    }

    /**
     * Sets first vaccine.
     *
     * @param firstVaccine the first vaccine
     */
    public void setFirstVaccine(VaccineInfo firstVaccine) {
        this.firstVaccine = firstVaccine;
    }

    /**
     * Gets second vaccine.
     *
     * @return the second vaccine
     */
    public VaccineInfo getSecondVaccine()
    {
        return secondVaccine;
    }

    /**
     * Sets second vaccine.
     *
     * @param secondVaccine the second vaccine
     */
    public void setSecondVaccine(VaccineInfo secondVaccine) {
        this.secondVaccine = secondVaccine;
    }
}
