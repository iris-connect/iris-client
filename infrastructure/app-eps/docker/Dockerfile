FROM inoeg/eps:v0.1.32

COPY conf /app/settings
COPY ca /app/settings/ca

ENV IRIS_ENV staging

ENV EPS_SETTINGS "settings/roles/$IRIS_ENV/app"

CMD ["--level", "trace", "server", "run"]
