import http from 'k6/http';


export default function () {

    // Генеруємо випадкову кількість елементів в масивах
    const numComments = Math.floor(Math.random() * 100) + 1; // Від 1 до 100
    const numAddresses = Math.floor(Math.random() * 100) + 1; // Від 1 до 100
    const numPhoneNumbers = Math.floor(Math.random() * 100) + 1; // Від 1 до 100

    // Генеруємо випадковий email
    const randomEmail = `tester${Math.floor(Math.random() * 10000000)}@test.com`;
    const randomEvent = Math.floor(Math.random() * 5); // Від 0 до 4

    // Генеруємо випадковий entityId (індекс ітерації)
    const entityId = __VU; // __VU - це індекс ітерації

    // Генеруємо масиви з випадковою кількістю елементів
    const comments = [];
    for (let i = 0; i < numComments; i++) {
        comments.push({
            text: "hi men",
            entityType: 0,
            entityId: entityId,
        });
    }

    const addresses = [];
    for (let i = 0; i < numAddresses; i++) {
        addresses.push({
            street: "Lomonosova",
            city: "Kyiv",
            state: "MyState",
        });
    }

    const phoneNumbers = [];
    for (let i = 0; i < numPhoneNumbers; i++) {
        phoneNumbers.push({
            number: "0988562102",
        });
    }

    const audits = [];
    for (let i = 0; i < numPhoneNumbers; i++) {
        audits.push({
            event: randomEvent,
        });
    }


    // Створюємо об'єкт payload з випадковими масивами та іншими полями
    const payload = {
        email: randomEmail,
        password: "admin",
        lastName: "fqdwfqdw",
        firstName: "fdwfwdf",
        rolesIds: [1, 2],
        matchingPassword: "admin",
        age: 40,
        gender: "MALE",
        addresses: addresses,
        comments: comments,
        phoneNumbers: phoneNumbers,
        audits: audits
    };

    // Відправляємо HTTP POST-запит
    const response = http.post('http://127.0.0.1:9090/auth/user/register', JSON.stringify(payload), {
        headers: {
            'Content-Type': 'application/json',
        },
    });

    // Виводимо результат запиту
    console.log(`Status Code: ${response.status}`);

    // Чекаємо певний час перед наступною ітерацією (наприклад, 1 секунда)
    // sleep(1);

}