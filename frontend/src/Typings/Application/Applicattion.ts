import { ReactNode } from "react";

export interface ServiceCardProps {
  title: string;
  description: string;
  details: string;
  image?: string;
  url?: string;
  onClick?: () => void; // 클릭 핸들러 추가
}

export interface EditFieldProps {
  label: string;
  defaultValue: string;
}

export interface EditableSectionProps {
  title: string;
  isEditing: boolean;
  handleEdit: () => void;
  children: ReactNode;
}
