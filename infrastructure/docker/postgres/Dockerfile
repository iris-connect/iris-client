FROM postgres:10-alpine

RUN apk update --no-cache && \
    apk upgrade --no-cache && \
    apk add --no-cache py-pip make gcc musl-dev && \
    pip install pgxnclient && \
    pgxnclient install temporal_tables
    
COPY psql.conf /etc/postgresql/postgresql.conf
