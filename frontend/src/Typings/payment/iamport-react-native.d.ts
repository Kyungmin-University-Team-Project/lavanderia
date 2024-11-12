declare module 'react-iamport' {
    export interface PaymentData {
        pg: string;
        pay_method: string;
        merchant_uid: string;
        amount: string;
        name: string;
        buyer_name: string;
        buyer_tel: string;
        buyer_email: string;
        buyer_addr: string;
        buyer_postcode: string;
    }

    interface IamportProps {
        userCode: string;
        data: PaymentData;
        callback: (response: any) => void;
    }

    export const Iamport: React.FC<IamportProps>;
}
