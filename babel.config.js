module.exports = {
    "presets": [
        "module:metro-react-native-babel-preset",
        '@babel/preset-react',
        [
            '@babel/preset-env',
            {
                targets: {
                    node: '14',
                },
            },
        ],
    ],
    "plugins": [
        "react-native-reanimated/plugin"
    ],
}