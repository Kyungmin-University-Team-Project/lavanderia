// src/Components/postcode/Postcode.tsx

import React from 'react';
import DaumPostcodeEmbed from 'react-daum-postcode';

interface PostcodeProps {
    onComplete: (address: string) => void;
}

const Postcode: React.FC<PostcodeProps> = ({ onComplete }) => {
    const handleComplete = (data: any) => {
        let fullAddress = data.address;
        let extraAddress = '';

        if (data.addressType === 'R') {
            if (data.bname !== '') {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '') {
                extraAddress += extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName;
            }
            fullAddress += extraAddress !== '' ? ` (${extraAddress})` : '';
        }

        onComplete(fullAddress); // 전달된 주소를 콜백으로 부모 컴포넌트에 전달
    };

    return <DaumPostcodeEmbed onComplete={handleComplete} />;
};

export default Postcode;
