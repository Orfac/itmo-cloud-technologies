docker build -t orfac/backend:v0 .
docker tag orfac/backend:v0 orfacello/itmo-cloud-backend:v0
docker push orfacello/itmo-cloud-backend:v0
