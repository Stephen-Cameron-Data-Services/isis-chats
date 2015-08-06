package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the schedules database table.
 * 
 */
@Entity
@Table(name="schedules")
public class Schedule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	private int dailyModulus;

	private String dailyType;

	private byte dailyWeekday;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date firstOccuranceDTTM;

	private BigInteger firstOccuranceDTTM_daysSinceEpoch;

	private BigInteger firstOccuranceDTTM_monthsSinceEpoch;

	private BigInteger firstOccuranceDTTM_weeksSinceEpoch;

	private BigInteger firstOccuranceDTTM_yearsSinceEpoch;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	private int monthlyDateDay;

	private int monthlyDateModulus;

	private String monthlyType;

	private int monthlyWeek;

	private String monthlyWeekDay;

	private int monthlyWeekDayModulus;

	@Temporal(TemporalType.TIMESTAMP)
	private Date nextOccuranceDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date periodBeginDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date periodEndDTTM;

	private int region;

	@Column(name="schedulerecurrancepattern_id")
	private BigInteger schedulerecurrancepatternId;

	private String title;

	private String weeklyDays;

	private int weeklyModulus;

	private int yearlyDateDay;

	private int yearlyDateMonth;

	private int yearlyModulus;

	private int yearlyMonthWeek;

	private int yearlyMonthWeekDay;

	private int yearlyMonthWeekMonth;

	private String yearlyType;

	public Schedule() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getCreatedbyUserId() {
		return this.createdbyUserId;
	}

	public void setCreatedbyUserId(BigInteger createdbyUserId) {
		this.createdbyUserId = createdbyUserId;
	}

	public Timestamp getCreatedDTTM() {
		return this.createdDTTM;
	}

	public void setCreatedDTTM(Timestamp createdDTTM) {
		this.createdDTTM = createdDTTM;
	}

	public int getDailyModulus() {
		return this.dailyModulus;
	}

	public void setDailyModulus(int dailyModulus) {
		this.dailyModulus = dailyModulus;
	}

	public String getDailyType() {
		return this.dailyType;
	}

	public void setDailyType(String dailyType) {
		this.dailyType = dailyType;
	}

	public byte getDailyWeekday() {
		return this.dailyWeekday;
	}

	public void setDailyWeekday(byte dailyWeekday) {
		this.dailyWeekday = dailyWeekday;
	}

	public Date getDeletedDTTM() {
		return this.deletedDTTM;
	}

	public void setDeletedDTTM(Date deletedDTTM) {
		this.deletedDTTM = deletedDTTM;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getFirstOccuranceDTTM() {
		return this.firstOccuranceDTTM;
	}

	public void setFirstOccuranceDTTM(Date firstOccuranceDTTM) {
		this.firstOccuranceDTTM = firstOccuranceDTTM;
	}

	public BigInteger getFirstOccuranceDTTM_daysSinceEpoch() {
		return this.firstOccuranceDTTM_daysSinceEpoch;
	}

	public void setFirstOccuranceDTTM_daysSinceEpoch(BigInteger firstOccuranceDTTM_daysSinceEpoch) {
		this.firstOccuranceDTTM_daysSinceEpoch = firstOccuranceDTTM_daysSinceEpoch;
	}

	public BigInteger getFirstOccuranceDTTM_monthsSinceEpoch() {
		return this.firstOccuranceDTTM_monthsSinceEpoch;
	}

	public void setFirstOccuranceDTTM_monthsSinceEpoch(BigInteger firstOccuranceDTTM_monthsSinceEpoch) {
		this.firstOccuranceDTTM_monthsSinceEpoch = firstOccuranceDTTM_monthsSinceEpoch;
	}

	public BigInteger getFirstOccuranceDTTM_weeksSinceEpoch() {
		return this.firstOccuranceDTTM_weeksSinceEpoch;
	}

	public void setFirstOccuranceDTTM_weeksSinceEpoch(BigInteger firstOccuranceDTTM_weeksSinceEpoch) {
		this.firstOccuranceDTTM_weeksSinceEpoch = firstOccuranceDTTM_weeksSinceEpoch;
	}

	public BigInteger getFirstOccuranceDTTM_yearsSinceEpoch() {
		return this.firstOccuranceDTTM_yearsSinceEpoch;
	}

	public void setFirstOccuranceDTTM_yearsSinceEpoch(BigInteger firstOccuranceDTTM_yearsSinceEpoch) {
		this.firstOccuranceDTTM_yearsSinceEpoch = firstOccuranceDTTM_yearsSinceEpoch;
	}

	public BigInteger getLastmodifiedbyUserId() {
		return this.lastmodifiedbyUserId;
	}

	public void setLastmodifiedbyUserId(BigInteger lastmodifiedbyUserId) {
		this.lastmodifiedbyUserId = lastmodifiedbyUserId;
	}

	public Date getLastmodifiedDTTM() {
		return this.lastmodifiedDTTM;
	}

	public void setLastmodifiedDTTM(Date lastmodifiedDTTM) {
		this.lastmodifiedDTTM = lastmodifiedDTTM;
	}

	public int getMonthlyDateDay() {
		return this.monthlyDateDay;
	}

	public void setMonthlyDateDay(int monthlyDateDay) {
		this.monthlyDateDay = monthlyDateDay;
	}

	public int getMonthlyDateModulus() {
		return this.monthlyDateModulus;
	}

	public void setMonthlyDateModulus(int monthlyDateModulus) {
		this.monthlyDateModulus = monthlyDateModulus;
	}

	public String getMonthlyType() {
		return this.monthlyType;
	}

	public void setMonthlyType(String monthlyType) {
		this.monthlyType = monthlyType;
	}

	public int getMonthlyWeek() {
		return this.monthlyWeek;
	}

	public void setMonthlyWeek(int monthlyWeek) {
		this.monthlyWeek = monthlyWeek;
	}

	public String getMonthlyWeekDay() {
		return this.monthlyWeekDay;
	}

	public void setMonthlyWeekDay(String monthlyWeekDay) {
		this.monthlyWeekDay = monthlyWeekDay;
	}

	public int getMonthlyWeekDayModulus() {
		return this.monthlyWeekDayModulus;
	}

	public void setMonthlyWeekDayModulus(int monthlyWeekDayModulus) {
		this.monthlyWeekDayModulus = monthlyWeekDayModulus;
	}

	public Date getNextOccuranceDTTM() {
		return this.nextOccuranceDTTM;
	}

	public void setNextOccuranceDTTM(Date nextOccuranceDTTM) {
		this.nextOccuranceDTTM = nextOccuranceDTTM;
	}

	public Date getPeriodBeginDTTM() {
		return this.periodBeginDTTM;
	}

	public void setPeriodBeginDTTM(Date periodBeginDTTM) {
		this.periodBeginDTTM = periodBeginDTTM;
	}

	public Date getPeriodEndDTTM() {
		return this.periodEndDTTM;
	}

	public void setPeriodEndDTTM(Date periodEndDTTM) {
		this.periodEndDTTM = periodEndDTTM;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public BigInteger getSchedulerecurrancepatternId() {
		return this.schedulerecurrancepatternId;
	}

	public void setSchedulerecurrancepatternId(BigInteger schedulerecurrancepatternId) {
		this.schedulerecurrancepatternId = schedulerecurrancepatternId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWeeklyDays() {
		return this.weeklyDays;
	}

	public void setWeeklyDays(String weeklyDays) {
		this.weeklyDays = weeklyDays;
	}

	public int getWeeklyModulus() {
		return this.weeklyModulus;
	}

	public void setWeeklyModulus(int weeklyModulus) {
		this.weeklyModulus = weeklyModulus;
	}

	public int getYearlyDateDay() {
		return this.yearlyDateDay;
	}

	public void setYearlyDateDay(int yearlyDateDay) {
		this.yearlyDateDay = yearlyDateDay;
	}

	public int getYearlyDateMonth() {
		return this.yearlyDateMonth;
	}

	public void setYearlyDateMonth(int yearlyDateMonth) {
		this.yearlyDateMonth = yearlyDateMonth;
	}

	public int getYearlyModulus() {
		return this.yearlyModulus;
	}

	public void setYearlyModulus(int yearlyModulus) {
		this.yearlyModulus = yearlyModulus;
	}

	public int getYearlyMonthWeek() {
		return this.yearlyMonthWeek;
	}

	public void setYearlyMonthWeek(int yearlyMonthWeek) {
		this.yearlyMonthWeek = yearlyMonthWeek;
	}

	public int getYearlyMonthWeekDay() {
		return this.yearlyMonthWeekDay;
	}

	public void setYearlyMonthWeekDay(int yearlyMonthWeekDay) {
		this.yearlyMonthWeekDay = yearlyMonthWeekDay;
	}

	public int getYearlyMonthWeekMonth() {
		return this.yearlyMonthWeekMonth;
	}

	public void setYearlyMonthWeekMonth(int yearlyMonthWeekMonth) {
		this.yearlyMonthWeekMonth = yearlyMonthWeekMonth;
	}

	public String getYearlyType() {
		return this.yearlyType;
	}

	public void setYearlyType(String yearlyType) {
		this.yearlyType = yearlyType;
	}

}