
## 1. 비동기 방식 테스트

```
curl -X POST http://localhost:8080/rest-jms-sample/async \
--h 'Content-Type: application/json' \
--d '{
         "id": "0001",
         "type": "donut",
         "name": "Cake",
         "ppu": 0.55,
         "batters": {
             "batter": [
                 {
                     "id": "1001",
                     "type": "Regular"
                 },
                 {
                     "id": "1002",
                     "type": "Chocolate"
                 }
             ]
         },
         "topping": [
             {
                 "id": "5001",
                 "type": "None"
             },
             {
                 "id": "5002",
                 "type": "Glazed"
             }
         ]
     }'
```

## 2. 동기 방식 테스트
```
curl -X POST http://localhost:8080/rest-jms-sample/sync \
--h 'Content-Type: application/json' \
--d '{
         "id": "0001",
         "type": "donut",
         "name": "Cake",
         "ppu": 0.55,
         "batters": {
             "batter": [
                 {
                     "id": "1001",
                     "type": "Regular"
                 },
                 {
                     "id": "1002",
                     "type": "Chocolate"
                 }
             ]
         },
         "topping": [
             {
                 "id": "5001",
                 "type": "None"
             },
             {
                 "id": "5002",
                 "type": "Glazed"
             }
         ]
     }'
```