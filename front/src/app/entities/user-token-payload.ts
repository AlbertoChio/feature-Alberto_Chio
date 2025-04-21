export interface UserTokenPayload {
  role: 'ROLE_CUSTOMER' | 'ROLE_UNKNOWN';
  name: 'Unknown';
  userId: string;
  sub: string;
  iat: Date;
  exp: Date;
}
