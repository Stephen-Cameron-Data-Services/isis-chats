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
				<xsl:call-template name="ClientSessionTotals" />
				<xsl:call-template name="CaseSessionTotals" />
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

	<xsl:template name="ClientSessionTotals">
		<table>
			<thead>
				<tr>
					<th colspan="2">Client Total Session Time (Minutes)</th>
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
							<xsl:value-of
								select="sum(/DEXFileUpload/Sessions/Session[SessionClients/SessionClient/ClientId=$clientId]/TimeMinutes)" />
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template name="CaseSessionTotals">
		<table>
			<thead>
				<tr>
					<th colspan="2">Case Total Session Time (Minutes)</th>
				</tr>
			</thead>
			<tbody>
				<xsl:for-each select="DEXFileUpload/Cases/Case/CaseId">
					<tr>
						<xsl:variable name="caseId">
							<xsl:value-of select="." />
						</xsl:variable>
						<td>
							<xsl:value-of select="$caseId" />
						</td>
						<td>
							<xsl:value-of
								select="sum(/DEXFileUpload/Sessions/Session[CaseId=$caseId]/TimeMinutes)" />
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
</xsl:stylesheet>