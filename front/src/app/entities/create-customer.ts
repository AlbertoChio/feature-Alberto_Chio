export interface CreateCustomer {
    "firstName": string,
    "lastName": string,
    "secondLastName": string,
    "dateOfBirth": Date,
    "gender": "Hombre" | "Mujer" | null,
    "placeOfBirth": "CDMX" | "SONORA" | null,
    "curp": string | null
}