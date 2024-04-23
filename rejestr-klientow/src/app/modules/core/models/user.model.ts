export interface UserLoginData {
  username: string;
  password: string;
}

export interface GetUsersResponse {
  id: number;
  email: string;
  password: string;
  username: string;
}

export type PostUserResponse = GetUsersResponse;

export type PostUser = Omit<GetUsersResponse, 'id'>;

export class User {
  constructor(public email: string, public username: string) {
  }
}
