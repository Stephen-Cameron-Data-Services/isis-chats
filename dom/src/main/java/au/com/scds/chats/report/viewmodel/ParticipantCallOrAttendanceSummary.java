/*
*
*  Copyright 2015 Stephen Cameron Data Services
*
*
*  Licensed under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package au.com.scds.chats.report.viewmodel;


import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.ViewModel;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import au.com.scds.chats.report.dex.DEXBulkUploadReport2;
import au.com.scds.chats.report.view.ParticipantCallOrAttendance;

@ViewModel
@DomainObject(objectType="ParticipantCallOrAttendanceSummary")
public class ParticipantCallOrAttendanceSummary implements HasAtPath {

	private Long participantId;
	private String surname;
	private String firstName;
	private Integer age;
	private String regionName;
	private String participantStatus;
	private Integer totalMinutes = 0;
	private Integer totalAdjustedMinutes = 0;

	public ParticipantCallOrAttendanceSummary() {

	}

	public ParticipantCallOrAttendanceSummary(ParticipantCallOrAttendance time) {
		setParticipantId(time.getParticipantId());
		setFirstName(time.getFirstName());
		setSurname(time.getSurname());
		setAge(time.getAge());
		setRegionName(time.getRegionName());
		setParticipantStatus(time.getParticipantStatus());
	}
	
	public String title(){
		return getFirstName() + " " + getSurname() + " (" + getAge() + ")" ;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public Integer getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(Integer totalMinutes) {
		this.totalMinutes = totalMinutes;
	}

	public Integer getTotalAdjustedMinutes() {
		return totalAdjustedMinutes;
	}

	public void setTotalAdjustedMinutes(Integer totalAdjustedMinutes) {
		this.totalAdjustedMinutes = totalAdjustedMinutes;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
	}

	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}

	@Programmatic
	public void addTime(ParticipantCallOrAttendance time) {
		if (time != null) {
			setTotalMinutes(getTotalMinutes() + time.getMinutes());
			if (time.getName().equals("CALL")) {
				setTotalAdjustedMinutes(getTotalAdjustedMinutes() + time.getMinutes());
			} else {
				setTotalAdjustedMinutes(getTotalAdjustedMinutes() + DEXBulkUploadReport2.adjustTimeForTransport(
						time.getMinutes(), time.getArrivingTransport(), time.getDepartingTransport()));
			}
		}
	}

	@Override
	public String getAtPath() {
		if (getRegionName().equals("STATEWIDE"))
			return "/";
		else {
			return "/" + getRegionName() + "_";
		}
	}
}
