import React from "react";
import {InputFieldProps} from "../../Typings/auth/signup";

const InputField: React.FC<InputFieldProps> = ({
                                                   id,
                                                   label,
                                                   type,
                                                   placeholder,
                                                   value,
                                                   onChange,
                                                   error,
                                                   icon,
                                                   onFocus,
                                                   inputRef,
                                                   onClick, // onClick 추가
                                               }) => (
    <div className="mb-4">
        <label htmlFor={id} className="block text-sm font-medium text-gray-700">
            {label}
        </label>
        <div
            className="flex items-center rounded-md border border-gray-300 shadow-sm"
            onClick={onClick} // onClick 추가
        >
            {icon}
            <input
                id={id}
                type={type}
                className="w-full px-3 py-2 text-base focus:border-indigo-500 focus:outline-none focus:ring-indigo-500"
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                onFocus={onFocus}
                ref={inputRef}
                required
            />
        </div>
        {error && <p className="text-sm text-red-500">{error}</p>}
    </div>
);

export default InputField;
