FROM inoeg/proxy:v0.1.63

COPY conf /app/settings
COPY ca /app/settings/ca

CMD ["--level", "trace", "run", "private"]
