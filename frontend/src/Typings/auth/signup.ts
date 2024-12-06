import React, { ChangeEvent } from 'react';

export interface FormData {
    id: string;
    password: string;
    email: string;
    name: string;
    dob: string;
    phone: string;
    address: string;
    detailedAddress: string;
    consentPromo: string;
    marketingConsent: string;
}

export interface Errors {
    id: string;
    password: string;
    email: string;
    name: string;
    dob: string;
    phone: string;
    consentPromo: string;
}

export interface InputFieldProps {
    id: string;
    label: string;
    type: string;
    placeholder: string;
    value: string;
    onChange: (event: ChangeEvent<HTMLInputElement>) => void;
    error?: string;
    icon: React.ReactNode;
    onFocus: () => void;
    inputRef: React.RefObject<HTMLInputElement>;
    onClick?: () => void; // onClick 속성 추가
}
