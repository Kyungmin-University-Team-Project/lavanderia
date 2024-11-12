module.exports = {
  purge: ["./src/**/*.{js,jsx,ts,tsx}"],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      fontFamily: {
        roboto: ["Roboto", "SCDream"],
        courgette: ["Courgette", "cursive"],
      },
      screens: {
        '3xl': '1600px', // 3xl 브레이크포인트 추가
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [
    require("tailwindcss"),
    require("@tailwindcss/forms"), // 폼 스타일 플러그인 추가
    require("postcss-preset-env")({
      stage: 0,
    }),
  ],
};
