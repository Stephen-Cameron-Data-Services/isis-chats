<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:xi="http://www.w3.org/2001/XInclude"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	exclude-result-prefixes="xsl html">
	<xsl:output method="html" />
	<xsl:template match="/">
		<html>
			<head>
				<style type="text/css">
					table, td {border:solid 1px grey; border-collapse:
					collapse;}
				</style>
			</head>
			<body>
				<xsl:apply-templates select="DEXFileUpload/*" />
				<xsl:call-template name="ClientInSessions" />
				<xsl:call-template name="CaseTotalSessionTimeByClientCount"/>

			</body>
		</html>
	</xsl:template>
	<xsl:template match="Clients">
		<table>
			<thead>
				<tr>
					<th colspan="2">CLIENTS</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates />
			</tbody>
		</table>
	</xsl:template>
	<xsl:template match="Cases">
		<table>
			<thead>
				<tr>
					<th colspan="2">CASES</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates />
			</tbody>
		</table>
	</xsl:template>
	<xsl:template match="Sessions">
		<table>
			<thead>
				<tr>
					<th colspan="2">SESSIONS</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates />
			</tbody>
		</table>
	</xsl:template>
	<xsl:template match="Client">
		<tr>
			<td colspan="2">
				CLIENT:
			</td>
		</tr>
		<xsl:for-each select="*">
			<tr>
				<td>
					<xsl:value-of select="name(.)" />
				</td>
				<td>
					<xsl:apply-templates />
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="Case">
		<tr>
			<td colspan="2">
				CASE:
			</td>
		</tr>
		<xsl:for-each select="*">
			<tr>
				<td>
					<xsl:value-of select="name(.)" />
				</td>
				<td>
					<xsl:apply-templates />
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="Session">
		<tr>
			<td colspan="2">
				SESSION:
			</td>
		</tr>
		<xsl:for-each select="*">
			<tr>
				<td>
					<xsl:value-of select="name(.)" />
				</td>
				<td>
					<xsl:apply-templates />
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="CaseClient">
		<div>
			<xsl:apply-templates />
		</div>
	</xsl:template>
	<xsl:template match="SessionClient">
		<div>
			<xsl:apply-templates />
		</div>
	</xsl:template>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template name="ClientInSessions">
		<table>
			<thead>
				<tr>
					<th colspan="2">Client in Session Lists</th>
				</tr>
			</thead>
			<tbody>
				<xsl:for-each select="DEXFileUpload/Clients/Client/ClientId">
					<tr>
						<xsl:variable name="clientId">
							<xsl:value-of select="." />
						</xsl:variable>
						<td>
							<xsl:value-of select="$clientId" />
						</td>
						<td>
							<xsl:for-each
								select="/DEXFileUpload/Sessions/Session[SessionClients/SessionClient/ClientId=$clientId]">
								<xsl:value-of select="SessionId" />
								<br />
							</xsl:for-each>
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template name="CaseTotalSessionTimeByClientCount">
		<table>
			<thead>
				<tr>
					<th colspan="2">Summed Case Session TimeMinutes * Count of SessionClients</th>
				</tr>
			</thead>
			<tbody>
				<xsl:for-each select="DEXFileUpload/Cases/Case">
					<tr>
						<td>
							<xsl:value-of select="CaseId" />
						</td>
						<td>
							<xsl:call-template name="findcasesum">
								<xsl:with-param name="caseId" select="./CaseId" />
								<xsl:with-param name="posn" select="1" />
								<xsl:with-param name="sum" select="0" />
							</xsl:call-template>
						</td>
					</tr>
				</xsl:for-each>
					<tr>
						<td>
							TOTAL
						</td>
						<td>
							<xsl:call-template name="findtotalsum">
								<xsl:with-param name="posn" select="1" />
								<xsl:with-param name="sum" select="0" />
							</xsl:call-template>
						</td>
					</tr>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template name="findcasesum">
		<xsl:param name="caseId" />
		<xsl:param name="posn" />
		<xsl:param name="sum" />
		<xsl:variable name="running-total" select="$sum + (/DEXFileUpload/Sessions/Session[CaseId=$caseId][$posn]/TimeMinutes * count(/DEXFileUpload/Sessions/Session[CaseId=$caseId][$posn]/SessionClients/SessionClient))"/>
		<xsl:choose>
			<xsl:when test="$posn = count(/DEXFileUpload/Sessions/Session[CaseId=$caseId])">
				<xsl:value-of select="$running-total"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="findcasesum">
					<xsl:with-param name="caseId" select="$caseId" />
					<xsl:with-param name="posn" select="$posn + 1" />
					<xsl:with-param name="sum" select="$running-total" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="findtotalsum">
		<xsl:param name="posn" />
		<xsl:param name="sum" />
		<xsl:variable name="running-total" select="$sum + (/DEXFileUpload/Sessions/Session[$posn]/TimeMinutes * count(/DEXFileUpload/Sessions/Session[$posn]/SessionClients/SessionClient))"/>
		<xsl:choose>
			<xsl:when test="$posn = count(/DEXFileUpload/Sessions/Session)">
				<xsl:value-of select="$running-total"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="findtotalsum">
					<xsl:with-param name="posn" select="$posn + 1" />
					<xsl:with-param name="sum" select="$running-total" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>