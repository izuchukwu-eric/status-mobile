module.exports = {
    "preset": "react-native",
    "setupFilesAfterEnv": ["@testing-library/jest-native/extend-expect"
        , "../jestSetup.js"
    ],
    "setupFiles": [
    ],
    "testPathIgnorePatterns": [
    ],
    "testTimeout": 60000,
    "transformIgnorePatterns": [
        "/node_modules/(?!(@react-native|react-native-image-crop-picker|@react-native-community|react-native-linear-gradient|react-native-background-timer|react-native|rn-emoji-keyboard|react-native-languages|react-native-shake|react-native-reanimated)/).*/"
    ],
    "globals": {
        "__TEST__": true
    },
    "testEnvironment": "node",
    rootDir: "component-test",
    testMatch: [
        "**/*__tests__*"
    ]
}