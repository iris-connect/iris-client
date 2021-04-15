{{/*
Expand the name of the chart.
*/}}
{{- define "iris-gateway.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "iris-gateway.fullname" -}}
{{- $name := .Chart.Name }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}

{{- define "iris-gateway.public" -}}
{{- include "iris-gateway.fullname" . }}-public
{{- end }}

{{- define "iris-gateway.public-labels" -}}
app.kubernetes.io/name: {{ include "iris-gateway.public" . }}
{{- end }}

{{- define "iris-gateway.location" -}}
{{- include "iris-gateway.fullname" . }}-location
{{- end }}

{{- define "iris-gateway.location-labels" -}}
app.kubernetes.io/name: {{ include "iris-gateway.location" . }}
{{- end }}

{{- define "iris-gateway.postgres" -}}
{{ include "iris-gateway.fullname" . }}-postgres
{{- end }}

{{- define "iris-gateway.postgres-labels" -}}
app.kubernetes.io/name: {{ include "iris-gateway.postgres" . }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "iris-gateway.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}
