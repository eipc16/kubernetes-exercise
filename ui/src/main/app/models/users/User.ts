export interface UserInterface {
    id: number | null;
    name: string;
    surname: string;
    username: string;
    password: string;
    email: string;
    phoneNumber: string;
    role: string;
}

export enum UserIdentifiers {
    EMAIL = "email",
    USERNAME = "username"
}