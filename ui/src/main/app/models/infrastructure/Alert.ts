export enum AlertTypes {
    SUCCESS = "success",
    WARNING = "warning",
    INFO = "info",
    ERROR = "error"
}

export interface AlertInterface {
    id: string;
    component: string;
    message: string;
    type: AlertTypes;
    duration?: number;
    canDismiss: boolean;
}