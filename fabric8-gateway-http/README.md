# fabric8-gateway-http
This should be deployed onto a Fabric8 node running the Gateway HTTP profile.

## What does it do?
Gets the JSON from the Gateway and checks it contains everything expected.

## How to use it?
Set loadbalancer to:

- Type: GET
- URL: http://{gateway-ip}:8080/http-healthcheck
- Response: HTTPCode == 200 && BodyContent == true
