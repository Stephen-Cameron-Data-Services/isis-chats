#DROP VIEW ActivityAttendanceSummary;
CREATE VIEW ActivityAttendanceSummary 
AS 
SELECT 
  activity.activity_id as activityId, 
  activity.name AS activityName, 
  activity.region_name AS regionName, 
  activity.startdatetime AS startDateTime, 
  activity.cancelled, 
  sum(case when attend.attended = TRUE then 1 else 0 end) as attendedCount, 
  sum(case when attend.attended = FALSE then 1 else 0 end) as notAttendedCount, 
  sum(case when attend.attended = TRUE AND not isnull(attend.startdatetime) AND not isnull(attend.enddatetime) then 1 else 0 end) as hasStartAndEndDateTimesCount,
  sum(case when attend.attended = TRUE AND not isnull(attend.arrivingtransporttype_name) AND not isnull(attend.departingtransporttype_name) then 1 else 0 end) as hasArrivingAndDepartingTransportCount,
  MIN((TO_SECONDS(attend.enddatetime) - TO_SECONDS(attend.startdatetime))) AS minTimeDiff,
  MAX((TO_SECONDS(attend.enddatetime) - TO_SECONDS(attend.startdatetime))) AS maxTimeDiff
FROM  
  activity  
LEFT OUTER JOIN
  attend
ON
   attend.activity_activity_id = activity.activity_id 
WHERE 
  activity.classifier <> 'RECURRING_ACTIVITY'
GROUP BY
  activity.activity_id;


#DROP VIEW ActivityYearMonth;
CREATE VIEW ActivityYearMonth AS
SELECT
  activity.name as activityName,
  activity.region_name as regionName,
  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as yearMonth
FROM
  activity
WHERE
  activity.classifier in ('ACTIVITY','PACTIVITY')
GROUP BY
  activity.name,
  activity.region_name,
  EXTRACT(YEAR_MONTH FROM activity.startdatetime);

CREATE
VIEW `inactiveparticipant` AS
    SELECT 
        `person`.`person_id` AS `personId`,
        `person`.`surname` AS `surname`,
        `person`.`firstname` AS `firstName`,
        `person`.`birthdate` AS `birthDate`,
        `activity`.`activity_id` AS `activityId`,
        `activity`.`startdatetime` AS `startDateTime`,
        `activity`.`name` AS `activityName`,
        `activity`.`region_name` AS `regionName`,
        (TO_DAYS(NOW()) - TO_DAYS(`attend`.`startdatetime`)) AS `daysSinceLastattended`
    FROM
        (((`attend`
        JOIN `participant`)
        JOIN `person`)
        JOIN `activity`)
    WHERE
        ((`participant`.`participant_id` = `attend`.`participant_participant_id`)
            AND (`activity`.`activity_id` = `attend`.`activity_activity_id`)
            AND (`participant`.`person_person_id` = `person`.`person_id`)
            AND (`participant`.`status` = 'ACTIVE')
            AND (`attend`.`attended` = TRUE))
    ORDER BY (TO_DAYS(NOW()) - TO_DAYS(`attend`.`startdatetime`)) ASC;
    
#DROP VIEW MailMergeData;
CREATE VIEW MailMergeData AS
SELECT 
  person.salutation_name AS salutation, 
  person.surname, 
  person.firstname AS firstName, 
  person.middlename AS middleName, 
  person.preferredname AS preferredName, 
  person.birthdate AS birthDate, 
  timestampdiff(year,person.birthdate,curdate()) AS age, 
  person.homephonenumber AS homePhoneNumber, 
  person.mobilephonenumber AS mobilePhoneNumber, 
  person.emailaddress AS emailAddress, 
  person.region_name AS regionOfPerson, 
  participant.status AS participantStatus, 
  volunteer.status AS volunteerStatus, 
  location.street1, 
  location.street2, 
  location.suburb, 
  location.postcode 
FROM 
  person 
LEFT OUTER JOIN 
  participant 
ON 
  participant.person_person_id = person.person_id  
LEFT OUTER JOIN 
  volunteer 
ON 
  volunteer.person_person_id = person.person_id 
LEFT OUTER JOIN 
  location 
ON 
  location.location_id = person.mailaddress_location_id;

#DROP VIEW ParticipantActivityByMonth;
CREATE VIEW ParticipantActivityByMonth AS
SELECT 
  person.person_id AS personId,						
  person.surname,
  person.firstname AS firstName,
  person.birthdate AS birthDate,
  activity.activity_id AS activityId,
  activity.region_name AS regionName,
  activity.name AS activityName,
  participant.participant_id AS participantId,
  participant.status AS participantStatus,
  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as yearMonth,
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,attend.startdatetime,attend.enddatetime))/60,1) as hoursAttended 
FROM 
  activity,
  attend,						
  participant,
  person 
WHERE 
  attend.activity_activity_id = activity.activity_id AND 
  participant.participant_id = attend.participant_participant_id AND 
  person.person_id = participant.person_person_id AND 							
  attend.attended = true 						
GROUP BY 
  participant.participant_id,
  activity.activity_id,
  EXTRACT(YEAR_MONTH FROM activity.startdatetime);
  
#DROP VIEW ParticipantActivityByMonthForDEX;
CREATE VIEW ParticipantActivityByMonthForDEX  AS 
SELECT 
  person.person_id AS personId, 						
  person.surname, 
  person.firstname AS firstName, 
  person.birthdate AS birthDate,
  person.slk,
  timestampdiff(year,person.birthdate,curdate()) AS age,
  activity.abbreviatedName AS activityAbbreviatedName, 
  activity.region_name AS regionName, 						
  participant.participant_id AS participantId, 
  participant.status AS participantStatus, 
  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as yearMonth, 
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,attend.startdatetime,attend.enddatetime))/60,1) as hoursAttended 
FROM 
  activity, 
  attend, 						
  participant, 
  person 
WHERE 
  attend.activity_activity_id = activity.activity_id AND 
  participant.participant_id = attend.participant_participant_id AND 
  person.person_id = participant.person_person_id AND 							
  attend.attended = true 						
GROUP BY 
  participant.participant_id, 
  activity.abbreviatedName, 
  activity.region_name, 						
  EXTRACT(YEAR_MONTH FROM activity.startdatetime);  

#DROP VIEW ParticipantPerson;
CREATE VIEW ParticipantPerson AS
SELECT
  activity.name AS activityName,
  activity.startDateTime,
  activity.region_name AS regionName,
  person.surname,
  person.firstname AS firstName,
  person.preferredname AS preferredName,
  concat(location.street1,' ' ,location.street2, ' ', location.suburb) as address,
  person.homephonenumber AS homePhoneNumber,
  person.mobilephonenumber AS mobilePhoneNumber,
  limitinghealthissues AS limitingHealthIssues,
  otherlimitingfactors AS otherLimitingFactors
FROM
  activity
JOIN
  participation
ON
  participation.activity_activity_id = activity.activity_id
JOIN
  participant
ON
  participant.participant_id = participation.participant_participant_id
JOIN
  person
ON
  person.person_id = participant.person_person_id
LEFT JOIN
  location
ON
  location.location_id =  person.streetaddress_location_id
ORDER BY
  person.surname, person.firstname;

#DROP VIEW ActivityParticipantAttendance;
CREATE VIEW ActivityParticipantAttendance AS 
SELECT 
  person.person_id as personId,
  person.surname, 
  person.firstname AS firstName, 
  person.birthdate AS birthDate,
  TIMESTAMPDIFF(YEAR,person.birthdate, activity.startdatetime) AS ageAtDayOfActivity, 
  person.slk,
  activity.activity_id AS activityId,
  activity.name AS activityName, 
  activity.abbreviatedName AS activityAbbreviatedName,  
  activity.region_name AS regionName, 
  activity.startdatetime AS startDateTime,
  activity.oldid AS oldId,
  participant.participant_id AS participantId,
  participant.status AS participantStatus,
  participant.aboriginalOrTorresStraitIslanderOrigin_name AS aboriginalOrTsi,
  attend.attend_id AS attendId,
  attend.attended,
  attend.arrivingtransporttype_name AS arrivingTransportType, 
  attend.departingtransporttype_name AS departingTransporttype,	  
  TIMESTAMPDIFF(MINUTE,attend.startdatetime,attend.enddatetime) as minutesattended 
FROM 
  activity, 
  attend, 						
  participant, 
  person 
WHERE 
  attend.activity_activity_id = activity.activity_id AND 
  participant.participant_id = attend.participant_participant_id AND 
  person.person_id = participant.person_person_id AND
  activity.cancelled = false
ORDER BY
  activity.startdatetime, activity.abbreviatedname, activity.region_name;
 
#DROP VIEW ActivityVolunteerVolunteeredTime;
CREATE VIEW ActivityVolunteerVolunteeredTime AS 
SELECT 
  person.person_id as personId,
  person.surname, 
  person.firstname AS firstName, 
  person.birthdate AS birthDate,
  TIMESTAMPDIFF(YEAR,person.birthdate, activity.startdatetime) AS ageAtDateOfActivity,
  person.slk,
  activity.activity_id AS activityId,
  activity.name AS activityName, 
  activity.abbreviatedName AS activityAbbreviatedName,  
  activity.region_name AS regionName, 
  activity.startdatetime AS startDateTime,
  activity.cancelled AS cancelled, 
  volunteer.volunteer_id AS volunteerId,
  participant.participant_id AS participantId,
  participant.aboriginalOrTorresStraitIslanderOrigin_name AS aboriginalOrTsi,
  volunteer.status AS volunteerStatus,
  volunteeredtime.volunteeredtime_id AS volunteeredtimeId,
  volunteeredtime.includeasparticipation as includeAsParticipation,  
  TIMESTAMPDIFF(MINUTE,volunteeredtime.startdatetime,volunteeredtime.enddatetime) as minutes
FROM 
  volunteeredtime
JOIN  
  activity
ON
  activity.activity_id = volunteeredtime.activity_activity_id
JOIN
  volunteer
ON
  volunteer.volunteer_id = volunteeredtime.volunteer_volunteer_id
JOIN
  person
ON
  person.person_id = volunteer.person_person_id
LEFT OUTER JOIN
  participant
ON
  participant.volunteer_volunteer_id = volunteeredtime.volunteer_volunteer_id
WHERE
  volunteeredtime.role = 'VTACTIVITY'
ORDER BY
  activity.startdatetime, activity.abbreviatedname, activity.region_name; 

#DROP VIEW CallDurationParticipant; 
CREATE VIEW CallDurationParticipant 
AS 
SELECT 
  person.person_id AS personId, 						
  person.surname, 
  person.firstname AS firstName, 
  person.birthdate AS birthDate, 
  person.slk, 
  TIMESTAMPDIFF(YEAR,person.birthdate,curdate()) AS ageAtDayOfCall, 						
  participant.participant_id AS participantId, 						
  participant.region_name AS regionName, 
  participant.status AS participantStatus, 
  telephonecall.startdatetime AS startdatetime, 
  CAST(SUM(TIMESTAMPDIFF(MINUTE,telephonecall.startdatetime,telephonecall.enddatetime)) AS UNSIGNED) as callMinutesTotal 
FROM 
  telephonecall, 						
  participant, 
  person 
WHERE 
  participant.participant_id = telephonecall.participant_participant_id AND 
  person.person_id = participant.person_person_id;  
  
#DROP VIEW CallsDurationByParticipantAndDayForDEX; 
CREATE VIEW CallsDurationByParticipantAndDayForDEX 
AS 
SELECT 
  person.person_id AS personId, 						
  person.surname, 
  person.firstname AS firstName, 
  person.birthdate AS birthDate, 
  person.slk, 
  timestampdiff(year,person.birthdate,curdate()) AS ageAtDateOfCall, 						
  participant.participant_id AS participantId, 						
  participant.region_name AS regionName, 
  participant.status AS participantStatus, 
  participant.aboriginalOrTorresStraitIslanderOrigin_name AS aboriginalOrTsi, 
  DATE(telephonecall.startdatetime) as date, 
  CAST(SUM(TIMESTAMPDIFF(MINUTE,telephonecall.startdatetime,telephonecall.enddatetime)) AS UNSIGNED) as callMinutesTotal 
FROM 
  telephonecall, 						
  participant, 
  person 
WHERE 
  participant.participant_id = telephonecall.participant_participant_id AND 
  person.person_id = participant.person_person_id AND
  NOT isNull(telephonecall.startdatetime) AND 
  NOT isNull(telephonecall.enddatetime)  
GROUP BY 
  participant.participant_id, 
  DATE(telephonecall.startdatetime);
  
#DROP VIEW ParticipantIdentity; 
CREATE VIEW ParticipantIdentity 
AS 
SELECT 
   `person`.`person_id` AS `personId`,
   `participant`.`participant_id` AS `participantId`,
   `person`.`surname` AS `surname`,
   `person`.`firstname` AS `firstName`,
   `person`.`middlename` AS `middleName`,
   `person`.`preferredname` AS `preferredName`,
   `person`.`birthdate` AS `birthDate`,
   TIMESTAMPDIFF(YEAR,`person`.`birthdate`,CURDATE()) AS `age`,
   `participant`.`status` AS `status`,
   `participant`.`region_name` AS `region`
FROM
   `participant`
JOIN 
   `person` 
ON
   `person`.`person_id` = `participant`.`person_person_id`;
   
#DROP VIEW VolunteerIdentity; 
CREATE VIEW VolunteerIdentity 
AS 
SELECT 
   `person`.`person_id` AS `personId`,
   `volunteer`.`volunteer_id` AS `volunteerId`,
   `person`.`surname` AS `surname`,
   `person`.`firstname` AS `firstName`,
   `person`.`middlename` AS `middleName`,
   `person`.`preferredname` AS `preferredName`,
   `person`.`birthdate` AS `birthDate`,
   TIMESTAMPDIFF(YEAR,`person`.`birthdate`,CURDATE()) AS `age`,
   `volunteer`.`status` AS `status`,
   `volunteer`.`region_name` AS `region`
FROM
   `volunteer`
JOIN 
   `person` 
ON
   `person`.`person_id` = `volunteer`.`person_person_id`;

#DROP VIEW CombinedCallAndAttendance;
CREATE VIEW CombinedCallAndAttendance AS
                SELECT 
				  personId AS personId, 
				  surname AS surname, 
				  firstName AS firstName, 
				  birthDate AS birthDate, 
				  ageAtDayOfCall AS age, 
				  slk AS slk, 
				  participantId AS participantId, 
				  regionName AS regionName, 
				  participantStatus AS participantStatus, 
				  'CALL' AS name, 
				  'CALL' AS abbreviatedName, 
				  startDateTime AS startDateTime, 
				  callMinutesTotal AS minutes, 
				  'N/A' AS arrivingTransport, 
				  'N/A' AS departingTransport 
				FROM 
				  calldurationparticipant
				UNION 
				SELECT 
				  personId AS personId, 
				  surname AS surname, 
				  firstName AS firstName, 
				  birthDate AS birthDate, 
				  ageAtDayOfActivity AS age, 
				  slk AS slk, 
				  participantId AS participantId, 
				  regionName AS regionName, 
				  participantStatus AS participantStatus, 
				  activityName AS name, 
				  activityAbbreviatedName AS abbreviatedName, 
				  startDateTime AS startDateTime, 
				  minutesAttended AS minutes, 
				  arrivingTransportType AS arrivingTransport, 
				  departingTransportType AS departingTransport 
				FROM    activityparticipantattendance 
				WHERE    (attended = TRUE);