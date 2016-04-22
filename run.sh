#!/bin/sh


echo "GET"
curl http://localhost:8080/MyService/WebService/
echo ""

echo "POST"
curl -H "Content-Type: application/json" -X POST -d '{"testString":"xyz","testInt":123}' http://localhost:8080/MyService/WebService/
echo ""

echo "PUT"
curl -X PUT http://localhost:8080/MyService/WebService/?something="someText"
echo ""

echo "DELETE"
curl -X DELETE http://localhost:8080/MyService/WebService/de305d54-75b4-431b-adb2-eb6b9e546014
echo ""

echo "DELETE"
curl -v -X DELETE http://localhost:8080/MyService/WebService/notAUUID
echo ""
