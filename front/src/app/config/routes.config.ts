export const ROUTE_CONFIG = {
  login: 'auth',
  register: 'register',
  app: 'apz',
  home: 'home',
  historial: 'historial',
  loan: 'prestamo',
} as const;

export type RouteKey = keyof typeof ROUTE_CONFIG;
export type Route = (typeof ROUTE_CONFIG)[RouteKey];
