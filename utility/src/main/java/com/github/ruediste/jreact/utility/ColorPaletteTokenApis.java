package com.github.ruediste.jreact.utility;

import com.github.ruediste.jreact.color.OkLch;
import com.github.ruediste.jreact.style.ApiHelperProvider;
import com.github.ruediste.jreact.style.CoreTokenElementApis.ColorHelper;

public class ColorPaletteTokenApis {
    private ColorPaletteTokenApis() {
    }

    public interface ColorRedApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult red50() {
            return _helper().color(new OkLch(0.971, 0.013, 17.38));
        }

        default TResult red100() {
            return _helper().color(new OkLch(0.936, 0.032, 17.717));
        }

        default TResult red200() {
            return _helper().color(new OkLch(0.885, 0.062, 18.334));
        }

        default TResult red300() {
            return _helper().color(new OkLch(0.808, 0.114, 19.571));
        }

        default TResult red400() {
            return _helper().color(new OkLch(0.704, 0.191, 22.216));
        }

        default TResult red500() {
            return _helper().color(new OkLch(0.637, 0.237, 25.331));
        }

        default TResult red600() {
            return _helper().color(new OkLch(0.577, 0.245, 27.325));
        }

        default TResult red700() {
            return _helper().color(new OkLch(0.505, 0.213, 27.518));
        }

        default TResult red800() {
            return _helper().color(new OkLch(0.444, 0.177, 26.899));
        }

        default TResult red900() {
            return _helper().color(new OkLch(0.396, 0.141, 25.723));
        }

        default TResult red950() {
            return _helper().color(new OkLch(0.258, 0.092, 26.042));
        }
    }

    public interface ColorOrangeApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult orange50() {
            return _helper().color(new OkLch(0.98, 0.016, 73.684));
        }

        default TResult orange100() {
            return _helper().color(new OkLch(0.954, 0.038, 75.164));
        }

        default TResult orange200() {
            return _helper().color(new OkLch(0.901, 0.076, 70.697));
        }

        default TResult orange300() {
            return _helper().color(new OkLch(0.837, 0.128, 66.29));
        }

        default TResult orange400() {
            return _helper().color(new OkLch(0.75, 0.183, 55.934));
        }

        default TResult orange500() {
            return _helper().color(new OkLch(0.705, 0.213, 47.604));
        }

        default TResult orange600() {
            return _helper().color(new OkLch(0.646, 0.222, 41.116));
        }

        default TResult orange700() {
            return _helper().color(new OkLch(0.553, 0.195, 38.402));
        }

        default TResult orange800() {
            return _helper().color(new OkLch(0.47, 0.157, 37.304));
        }

        default TResult orange900() {
            return _helper().color(new OkLch(0.408, 0.123, 38.172));
        }

        default TResult orange950() {
            return _helper().color(new OkLch(0.266, 0.079, 36.259));
        }
    }

    public interface ColorAmberApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult amber50() {
            return _helper().color(new OkLch(0.987, 0.022, 95.277));
        }

        default TResult amber100() {
            return _helper().color(new OkLch(0.962, 0.059, 95.617));
        }

        default TResult amber200() {
            return _helper().color(new OkLch(0.924, 0.12, 95.746));
        }

        default TResult amber300() {
            return _helper().color(new OkLch(0.879, 0.169, 91.605));
        }

        default TResult amber400() {
            return _helper().color(new OkLch(0.828, 0.189, 84.429));
        }

        default TResult amber500() {
            return _helper().color(new OkLch(0.769, 0.188, 70.08));
        }

        default TResult amber600() {
            return _helper().color(new OkLch(0.666, 0.179, 58.318));
        }

        default TResult amber700() {
            return _helper().color(new OkLch(0.555, 0.163, 48.998));
        }

        default TResult amber800() {
            return _helper().color(new OkLch(0.473, 0.137, 46.201));
        }

        default TResult amber900() {
            return _helper().color(new OkLch(0.414, 0.112, 45.904));
        }

        default TResult amber950() {
            return _helper().color(new OkLch(0.279, 0.077, 45.635));
        }
    }

    public interface ColorYellowApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult yellow50() {
            return _helper().color(new OkLch(0.987, 0.026, 102.212));
        }

        default TResult yellow100() {
            return _helper().color(new OkLch(0.973, 0.071, 103.193));
        }

        default TResult yellow200() {
            return _helper().color(new OkLch(0.945, 0.129, 101.54));
        }

        default TResult yellow300() {
            return _helper().color(new OkLch(0.905, 0.182, 98.111));
        }

        default TResult yellow400() {
            return _helper().color(new OkLch(0.852, 0.199, 91.936));
        }

        default TResult yellow500() {
            return _helper().color(new OkLch(0.795, 0.184, 86.047));
        }

        default TResult yellow600() {
            return _helper().color(new OkLch(0.681, 0.162, 75.834));
        }

        default TResult yellow700() {
            return _helper().color(new OkLch(0.554, 0.135, 66.442));
        }

        default TResult yellow800() {
            return _helper().color(new OkLch(0.476, 0.114, 61.907));
        }

        default TResult yellow900() {
            return _helper().color(new OkLch(0.421, 0.095, 57.708));
        }

        default TResult yellow950() {
            return _helper().color(new OkLch(0.286, 0.066, 53.813));
        }
    }

    public interface ColorLimeApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult lime50() {
            return _helper().color(new OkLch(0.986, 0.031, 120.757));
        }

        default TResult lime100() {
            return _helper().color(new OkLch(0.967, 0.067, 122.328));
        }

        default TResult lime200() {
            return _helper().color(new OkLch(0.938, 0.127, 124.321));
        }

        default TResult lime300() {
            return _helper().color(new OkLch(0.897, 0.196, 126.665));
        }

        default TResult lime400() {
            return _helper().color(new OkLch(0.841, 0.238, 128.85));
        }

        default TResult lime500() {
            return _helper().color(new OkLch(0.768, 0.233, 130.85));
        }

        default TResult lime600() {
            return _helper().color(new OkLch(0.648, 0.2, 131.684));
        }

        default TResult lime700() {
            return _helper().color(new OkLch(0.532, 0.157, 131.589));
        }

        default TResult lime800() {
            return _helper().color(new OkLch(0.453, 0.124, 130.933));
        }

        default TResult lime900() {
            return _helper().color(new OkLch(0.405, 0.101, 131.063));
        }

        default TResult lime950() {
            return _helper().color(new OkLch(0.274, 0.072, 132.109));
        }
    }

    public interface ColorGreenApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult green50() {
            return _helper().color(new OkLch(0.982, 0.018, 155.826));
        }

        default TResult green100() {
            return _helper().color(new OkLch(0.962, 0.044, 156.743));
        }

        default TResult green200() {
            return _helper().color(new OkLch(0.925, 0.084, 155.995));
        }

        default TResult green300() {
            return _helper().color(new OkLch(0.871, 0.15, 154.449));
        }

        default TResult green400() {
            return _helper().color(new OkLch(0.792, 0.209, 151.711));
        }

        default TResult green500() {
            return _helper().color(new OkLch(0.723, 0.219, 149.579));
        }

        default TResult green600() {
            return _helper().color(new OkLch(0.627, 0.194, 149.214));
        }

        default TResult green700() {
            return _helper().color(new OkLch(0.527, 0.154, 150.069));
        }

        default TResult green800() {
            return _helper().color(new OkLch(0.448, 0.119, 151.328));
        }

        default TResult green900() {
            return _helper().color(new OkLch(0.393, 0.095, 152.535));
        }

        default TResult green950() {
            return _helper().color(new OkLch(0.266, 0.065, 152.934));
        }
    }

    public interface ColorEmeraldApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult emerald50() {
            return _helper().color(new OkLch(0.979, 0.021, 166.113));
        }

        default TResult emerald100() {
            return _helper().color(new OkLch(0.95, 0.052, 163.051));
        }

        default TResult emerald200() {
            return _helper().color(new OkLch(0.905, 0.093, 164.15));
        }

        default TResult emerald300() {
            return _helper().color(new OkLch(0.845, 0.143, 164.978));
        }

        default TResult emerald400() {
            return _helper().color(new OkLch(0.765, 0.177, 163.223));
        }

        default TResult emerald500() {
            return _helper().color(new OkLch(0.696, 0.17, 162.48));
        }

        default TResult emerald600() {
            return _helper().color(new OkLch(0.596, 0.145, 163.225));
        }

        default TResult emerald700() {
            return _helper().color(new OkLch(0.508, 0.118, 165.612));
        }

        default TResult emerald800() {
            return _helper().color(new OkLch(0.432, 0.095, 166.913));
        }

        default TResult emerald900() {
            return _helper().color(new OkLch(0.378, 0.077, 168.94));
        }

        default TResult emerald950() {
            return _helper().color(new OkLch(0.262, 0.051, 172.552));
        }
    }

    public interface ColorTealApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult teal50() {
            return _helper().color(new OkLch(0.984, 0.014, 180.72));
        }

        default TResult teal100() {
            return _helper().color(new OkLch(0.953, 0.051, 180.801));
        }

        default TResult teal200() {
            return _helper().color(new OkLch(0.91, 0.096, 180.426));
        }

        default TResult teal300() {
            return _helper().color(new OkLch(0.855, 0.138, 181.071));
        }

        default TResult teal400() {
            return _helper().color(new OkLch(0.777, 0.152, 181.912));
        }

        default TResult teal500() {
            return _helper().color(new OkLch(0.704, 0.14, 182.503));
        }

        default TResult teal600() {
            return _helper().color(new OkLch(0.6, 0.118, 184.704));
        }

        default TResult teal700() {
            return _helper().color(new OkLch(0.511, 0.096, 186.391));
        }

        default TResult teal800() {
            return _helper().color(new OkLch(0.437, 0.078, 188.216));
        }

        default TResult teal900() {
            return _helper().color(new OkLch(0.386, 0.063, 188.416));
        }

        default TResult teal950() {
            return _helper().color(new OkLch(0.277, 0.046, 192.524));
        }
    }

    public interface ColorCyanApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult cyan50() {
            return _helper().color(new OkLch(0.984, 0.019, 200.873));
        }

        default TResult cyan100() {
            return _helper().color(new OkLch(0.956, 0.045, 203.388));
        }

        default TResult cyan200() {
            return _helper().color(new OkLch(0.917, 0.08, 205.041));
        }

        default TResult cyan300() {
            return _helper().color(new OkLch(0.865, 0.127, 207.078));
        }

        default TResult cyan400() {
            return _helper().color(new OkLch(0.789, 0.154, 211.53));
        }

        default TResult cyan500() {
            return _helper().color(new OkLch(0.715, 0.143, 215.221));
        }

        default TResult cyan600() {
            return _helper().color(new OkLch(0.609, 0.126, 221.723));
        }

        default TResult cyan700() {
            return _helper().color(new OkLch(0.52, 0.105, 223.128));
        }

        default TResult cyan800() {
            return _helper().color(new OkLch(0.45, 0.085, 224.283));
        }

        default TResult cyan900() {
            return _helper().color(new OkLch(0.398, 0.07, 227.392));
        }

        default TResult cyan950() {
            return _helper().color(new OkLch(0.302, 0.056, 229.695));
        }
    }

    public interface ColorSkyApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult sky50() {
            return _helper().color(new OkLch(0.977, 0.013, 236.62));
        }

        default TResult sky100() {
            return _helper().color(new OkLch(0.951, 0.026, 236.824));
        }

        default TResult sky200() {
            return _helper().color(new OkLch(0.901, 0.058, 230.902));
        }

        default TResult sky300() {
            return _helper().color(new OkLch(0.828, 0.111, 230.318));
        }

        default TResult sky400() {
            return _helper().color(new OkLch(0.746, 0.16, 232.661));
        }

        default TResult sky500() {
            return _helper().color(new OkLch(0.685, 0.169, 237.323));
        }

        default TResult sky600() {
            return _helper().color(new OkLch(0.588, 0.158, 241.966));
        }

        default TResult sky700() {
            return _helper().color(new OkLch(0.5, 0.134, 242.749));
        }

        default TResult sky800() {
            return _helper().color(new OkLch(0.443, 0.11, 240.79));
        }

        default TResult sky900() {
            return _helper().color(new OkLch(0.391, 0.09, 240.876));
        }

        default TResult sky950() {
            return _helper().color(new OkLch(0.293, 0.066, 243.157));
        }
    }

    public interface ColorBlueApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult blue50() {
            return _helper().color(new OkLch(0.97, 0.014, 254.604));
        }

        default TResult blue100() {
            return _helper().color(new OkLch(0.932, 0.032, 255.585));
        }

        default TResult blue200() {
            return _helper().color(new OkLch(0.882, 0.059, 254.128));
        }

        default TResult blue300() {
            return _helper().color(new OkLch(0.809, 0.105, 251.813));
        }

        default TResult blue400() {
            return _helper().color(new OkLch(0.707, 0.165, 254.624));
        }

        default TResult blue500() {
            return _helper().color(new OkLch(0.623, 0.214, 259.815));
        }

        default TResult blue600() {
            return _helper().color(new OkLch(0.546, 0.245, 262.881));
        }

        default TResult blue700() {
            return _helper().color(new OkLch(0.488, 0.243, 264.376));
        }

        default TResult blue800() {
            return _helper().color(new OkLch(0.424, 0.199, 265.638));
        }

        default TResult blue900() {
            return _helper().color(new OkLch(0.379, 0.146, 265.522));
        }

        default TResult blue950() {
            return _helper().color(new OkLch(0.282, 0.091, 267.935));
        }
    }

    public interface ColorIndigoApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult indigo50() {
            return _helper().color(new OkLch(0.962, 0.018, 272.314));
        }

        default TResult indigo100() {
            return _helper().color(new OkLch(0.93, 0.034, 272.788));
        }

        default TResult indigo200() {
            return _helper().color(new OkLch(0.87, 0.065, 274.039));
        }

        default TResult indigo300() {
            return _helper().color(new OkLch(0.785, 0.115, 274.713));
        }

        default TResult indigo400() {
            return _helper().color(new OkLch(0.673, 0.182, 276.935));
        }

        default TResult indigo500() {
            return _helper().color(new OkLch(0.585, 0.233, 277.117));
        }

        default TResult indigo600() {
            return _helper().color(new OkLch(0.511, 0.262, 276.966));
        }

        default TResult indigo700() {
            return _helper().color(new OkLch(0.457, 0.24, 277.023));
        }

        default TResult indigo800() {
            return _helper().color(new OkLch(0.398, 0.195, 277.366));
        }

        default TResult indigo900() {
            return _helper().color(new OkLch(0.359, 0.144, 278.697));
        }

        default TResult indigo950() {
            return _helper().color(new OkLch(0.257, 0.09, 281.288));
        }
    }

    public interface ColorVioletApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult violet50() {
            return _helper().color(new OkLch(0.969, 0.016, 293.756));
        }

        default TResult violet100() {
            return _helper().color(new OkLch(0.943, 0.029, 294.588));
        }

        default TResult violet200() {
            return _helper().color(new OkLch(0.894, 0.057, 293.283));
        }

        default TResult violet300() {
            return _helper().color(new OkLch(0.811, 0.111, 293.571));
        }

        default TResult violet400() {
            return _helper().color(new OkLch(0.702, 0.183, 293.541));
        }

        default TResult violet500() {
            return _helper().color(new OkLch(0.606, 0.25, 292.717));
        }

        default TResult violet600() {
            return _helper().color(new OkLch(0.541, 0.281, 293.009));
        }

        default TResult violet700() {
            return _helper().color(new OkLch(0.491, 0.27, 292.581));
        }

        default TResult violet800() {
            return _helper().color(new OkLch(0.432, 0.232, 292.759));
        }

        default TResult violet900() {
            return _helper().color(new OkLch(0.38, 0.189, 293.745));
        }

        default TResult violet950() {
            return _helper().color(new OkLch(0.283, 0.141, 291.089));
        }
    }

    public interface ColorPurpleApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult purple50() {
            return _helper().color(new OkLch(0.977, 0.014, 308.299));
        }

        default TResult purple100() {
            return _helper().color(new OkLch(0.946, 0.033, 307.174));
        }

        default TResult purple200() {
            return _helper().color(new OkLch(0.902, 0.063, 306.703));
        }

        default TResult purple300() {
            return _helper().color(new OkLch(0.827, 0.119, 306.383));
        }

        default TResult purple400() {
            return _helper().color(new OkLch(0.714, 0.203, 305.504));
        }

        default TResult purple500() {
            return _helper().color(new OkLch(0.627, 0.265, 303.9));
        }

        default TResult purple600() {
            return _helper().color(new OkLch(0.558, 0.288, 302.321));
        }

        default TResult purple700() {
            return _helper().color(new OkLch(0.496, 0.265, 301.924));
        }

        default TResult purple800() {
            return _helper().color(new OkLch(0.438, 0.218, 303.724));
        }

        default TResult purple900() {
            return _helper().color(new OkLch(0.381, 0.176, 304.987));
        }

        default TResult purple950() {
            return _helper().color(new OkLch(0.291, 0.149, 302.717));
        }
    }

    public interface ColorFuchsiaApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult fuchsia50() {
            return _helper().color(new OkLch(0.977, 0.017, 320.058));
        }

        default TResult fuchsia100() {
            return _helper().color(new OkLch(0.952, 0.037, 318.852));
        }

        default TResult fuchsia200() {
            return _helper().color(new OkLch(0.903, 0.076, 319.62));
        }

        default TResult fuchsia300() {
            return _helper().color(new OkLch(0.833, 0.145, 321.434));
        }

        default TResult fuchsia400() {
            return _helper().color(new OkLch(0.74, 0.238, 322.16));
        }

        default TResult fuchsia500() {
            return _helper().color(new OkLch(0.667, 0.295, 322.15));
        }

        default TResult fuchsia600() {
            return _helper().color(new OkLch(0.591, 0.293, 322.896));
        }

        default TResult fuchsia700() {
            return _helper().color(new OkLch(0.518, 0.253, 323.949));
        }

        default TResult fuchsia800() {
            return _helper().color(new OkLch(0.452, 0.211, 324.591));
        }

        default TResult fuchsia900() {
            return _helper().color(new OkLch(0.401, 0.17, 325.612));
        }

        default TResult fuchsia950() {
            return _helper().color(new OkLch(0.293, 0.136, 325.661));
        }
    }

    public interface ColorPinkApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult pink50() {
            return _helper().color(new OkLch(0.971, 0.014, 343.198));
        }

        default TResult pink100() {
            return _helper().color(new OkLch(0.948, 0.028, 342.258));
        }

        default TResult pink200() {
            return _helper().color(new OkLch(0.899, 0.061, 343.231));
        }

        default TResult pink300() {
            return _helper().color(new OkLch(0.823, 0.12, 346.018));
        }

        default TResult pink400() {
            return _helper().color(new OkLch(0.718, 0.202, 349.761));
        }

        default TResult pink500() {
            return _helper().color(new OkLch(0.656, 0.241, 354.308));
        }

        default TResult pink600() {
            return _helper().color(new OkLch(0.592, 0.249, 0.584));
        }

        default TResult pink700() {
            return _helper().color(new OkLch(0.525, 0.223, 3.958));
        }

        default TResult pink800() {
            return _helper().color(new OkLch(0.459, 0.187, 3.815));
        }

        default TResult pink900() {
            return _helper().color(new OkLch(0.408, 0.153, 2.432));
        }

        default TResult pink950() {
            return _helper().color(new OkLch(0.284, 0.109, 3.907));
        }
    }

    public interface ColorRoseApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult rose50() {
            return _helper().color(new OkLch(0.969, 0.015, 12.422));
        }

        default TResult rose100() {
            return _helper().color(new OkLch(0.941, 0.03, 12.58));
        }

        default TResult rose200() {
            return _helper().color(new OkLch(0.892, 0.058, 10.001));
        }

        default TResult rose300() {
            return _helper().color(new OkLch(0.81, 0.117, 11.638));
        }

        default TResult rose400() {
            return _helper().color(new OkLch(0.712, 0.194, 13.428));
        }

        default TResult rose500() {
            return _helper().color(new OkLch(0.645, 0.246, 16.439));
        }

        default TResult rose600() {
            return _helper().color(new OkLch(0.586, 0.253, 17.585));
        }

        default TResult rose700() {
            return _helper().color(new OkLch(0.514, 0.222, 16.935));
        }

        default TResult rose800() {
            return _helper().color(new OkLch(0.455, 0.188, 13.697));
        }

        default TResult rose900() {
            return _helper().color(new OkLch(0.41, 0.159, 10.272));
        }

        default TResult rose950() {
            return _helper().color(new OkLch(0.271, 0.105, 12.094));
        }
    }

    public interface ColorSlateApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult slate50() {
            return _helper().color(new OkLch(0.984, 0.003, 247.858));
        }

        default TResult slate100() {
            return _helper().color(new OkLch(0.968, 0.007, 247.896));
        }

        default TResult slate200() {
            return _helper().color(new OkLch(0.929, 0.013, 255.508));
        }

        default TResult slate300() {
            return _helper().color(new OkLch(0.869, 0.022, 252.894));
        }

        default TResult slate400() {
            return _helper().color(new OkLch(0.704, 0.04, 256.788));
        }

        default TResult slate500() {
            return _helper().color(new OkLch(0.554, 0.046, 257.417));
        }

        default TResult slate600() {
            return _helper().color(new OkLch(0.446, 0.043, 257.281));
        }

        default TResult slate700() {
            return _helper().color(new OkLch(0.372, 0.044, 257.287));
        }

        default TResult slate800() {
            return _helper().color(new OkLch(0.279, 0.041, 260.031));
        }

        default TResult slate900() {
            return _helper().color(new OkLch(0.208, 0.042, 265.755));
        }

        default TResult slate950() {
            return _helper().color(new OkLch(0.129, 0.042, 264.695));
        }
    }

    public interface ColorGrayApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult gray50() {
            return _helper().color(new OkLch(0.985, 0.002, 247.839));
        }

        default TResult gray100() {
            return _helper().color(new OkLch(0.967, 0.003, 264.542));
        }

        default TResult gray200() {
            return _helper().color(new OkLch(0.928, 0.006, 264.531));
        }

        default TResult gray300() {
            return _helper().color(new OkLch(0.872, 0.01, 258.338));
        }

        default TResult gray400() {
            return _helper().color(new OkLch(0.707, 0.022, 261.325));
        }

        default TResult gray500() {
            return _helper().color(new OkLch(0.551, 0.027, 264.364));
        }

        default TResult gray600() {
            return _helper().color(new OkLch(0.446, 0.03, 256.802));
        }

        default TResult gray700() {
            return _helper().color(new OkLch(0.373, 0.034, 259.733));
        }

        default TResult gray800() {
            return _helper().color(new OkLch(0.278, 0.033, 256.848));
        }

        default TResult gray900() {
            return _helper().color(new OkLch(0.21, 0.034, 264.665));
        }

        default TResult gray950() {
            return _helper().color(new OkLch(0.13, 0.028, 261.692));
        }
    }

    public interface ColorZincApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult zinc50() {
            return _helper().color(new OkLch(0.985, 0, 0));
        }

        default TResult zinc100() {
            return _helper().color(new OkLch(0.967, 0.001, 286.375));
        }

        default TResult zinc200() {
            return _helper().color(new OkLch(0.92, 0.004, 286.32));
        }

        default TResult zinc300() {
            return _helper().color(new OkLch(0.871, 0.006, 286.286));
        }

        default TResult zinc400() {
            return _helper().color(new OkLch(0.705, 0.015, 286.067));
        }

        default TResult zinc500() {
            return _helper().color(new OkLch(0.552, 0.016, 285.938));
        }

        default TResult zinc600() {
            return _helper().color(new OkLch(0.442, 0.017, 285.786));
        }

        default TResult zinc700() {
            return _helper().color(new OkLch(0.37, 0.013, 285.805));
        }

        default TResult zinc800() {
            return _helper().color(new OkLch(0.274, 0.006, 286.033));
        }

        default TResult zinc900() {
            return _helper().color(new OkLch(0.21, 0.006, 285.885));
        }

        default TResult zinc950() {
            return _helper().color(new OkLch(0.141, 0.005, 285.823));
        }
    }

    public interface ColorNeutralApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult neutral50() {
            return _helper().color(new OkLch(0.985, 0, 0));
        }

        default TResult neutral100() {
            return _helper().color(new OkLch(0.97, 0, 0));
        }

        default TResult neutral200() {
            return _helper().color(new OkLch(0.922, 0, 0));
        }

        default TResult neutral300() {
            return _helper().color(new OkLch(0.87, 0, 0));
        }

        default TResult neutral400() {
            return _helper().color(new OkLch(0.708, 0, 0));
        }

        default TResult neutral500() {
            return _helper().color(new OkLch(0.556, 0, 0));
        }

        default TResult neutral600() {
            return _helper().color(new OkLch(0.439, 0, 0));
        }

        default TResult neutral700() {
            return _helper().color(new OkLch(0.371, 0, 0));
        }

        default TResult neutral800() {
            return _helper().color(new OkLch(0.269, 0, 0));
        }

        default TResult neutral900() {
            return _helper().color(new OkLch(0.205, 0, 0));
        }

        default TResult neutral950() {
            return _helper().color(new OkLch(0.145, 0, 0));
        }
    }

    public interface ColorStoneApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult stone50() {
            return _helper().color(new OkLch(0.985, 0.001, 106.423));
        }

        default TResult stone100() {
            return _helper().color(new OkLch(0.97, 0.001, 106.424));
        }

        default TResult stone200() {
            return _helper().color(new OkLch(0.923, 0.003, 48.717));
        }

        default TResult stone300() {
            return _helper().color(new OkLch(0.869, 0.005, 56.366));
        }

        default TResult stone400() {
            return _helper().color(new OkLch(0.709, 0.01, 56.259));
        }

        default TResult stone500() {
            return _helper().color(new OkLch(0.553, 0.013, 58.071));
        }

        default TResult stone600() {
            return _helper().color(new OkLch(0.444, 0.011, 73.639));
        }

        default TResult stone700() {
            return _helper().color(new OkLch(0.374, 0.01, 67.558));
        }

        default TResult stone800() {
            return _helper().color(new OkLch(0.268, 0.007, 34.298));
        }

        default TResult stone900() {
            return _helper().color(new OkLch(0.216, 0.006, 56.043));
        }

        default TResult stone950() {
            return _helper().color(new OkLch(0.147, 0.004, 49.25));
        }
    }

    public interface ColorMauveApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult mauve50() {
            return _helper().color(new OkLch(0.985, 0, 0));
        }

        default TResult mauve100() {
            return _helper().color(new OkLch(0.96, 0.003, 325.6));
        }

        default TResult mauve200() {
            return _helper().color(new OkLch(0.922, 0.005, 325.62));
        }

        default TResult mauve300() {
            return _helper().color(new OkLch(0.865, 0.012, 325.68));
        }

        default TResult mauve400() {
            return _helper().color(new OkLch(0.711, 0.019, 323.02));
        }

        default TResult mauve500() {
            return _helper().color(new OkLch(0.542, 0.034, 322.5));
        }

        default TResult mauve600() {
            return _helper().color(new OkLch(0.435, 0.029, 321.78));
        }

        default TResult mauve700() {
            return _helper().color(new OkLch(0.364, 0.029, 323.89));
        }

        default TResult mauve800() {
            return _helper().color(new OkLch(0.263, 0.024, 320.12));
        }

        default TResult mauve900() {
            return _helper().color(new OkLch(0.212, 0.019, 322.12));
        }

        default TResult mauve950() {
            return _helper().color(new OkLch(0.145, 0.008, 326));
        }
    }

    public interface ColorOliveApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult olive50() {
            return _helper().color(new OkLch(0.988, 0.003, 106.5));
        }

        default TResult olive100() {
            return _helper().color(new OkLch(0.966, 0.005, 106.5));
        }

        default TResult olive200() {
            return _helper().color(new OkLch(0.93, 0.007, 106.5));
        }

        default TResult olive300() {
            return _helper().color(new OkLch(0.88, 0.011, 106.6));
        }

        default TResult olive400() {
            return _helper().color(new OkLch(0.737, 0.021, 106.9));
        }

        default TResult olive500() {
            return _helper().color(new OkLch(0.58, 0.031, 107.3));
        }

        default TResult olive600() {
            return _helper().color(new OkLch(0.466, 0.025, 107.3));
        }

        default TResult olive700() {
            return _helper().color(new OkLch(0.394, 0.023, 107.4));
        }

        default TResult olive800() {
            return _helper().color(new OkLch(0.286, 0.016, 107.4));
        }

        default TResult olive900() {
            return _helper().color(new OkLch(0.228, 0.013, 107.4));
        }

        default TResult olive950() {
            return _helper().color(new OkLch(0.153, 0.006, 107.1));
        }
    }

    public interface ColorMistApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult mist50() {
            return _helper().color(new OkLch(0.987, 0.002, 197.1));
        }

        default TResult mist100() {
            return _helper().color(new OkLch(0.963, 0.002, 197.1));
        }

        default TResult mist200() {
            return _helper().color(new OkLch(0.925, 0.005, 214.3));
        }

        default TResult mist300() {
            return _helper().color(new OkLch(0.872, 0.007, 219.6));
        }

        default TResult mist400() {
            return _helper().color(new OkLch(0.723, 0.014, 214.4));
        }

        default TResult mist500() {
            return _helper().color(new OkLch(0.56, 0.021, 213.5));
        }

        default TResult mist600() {
            return _helper().color(new OkLch(0.45, 0.017, 213.2));
        }

        default TResult mist700() {
            return _helper().color(new OkLch(0.378, 0.015, 216));
        }

        default TResult mist800() {
            return _helper().color(new OkLch(0.275, 0.011, 216.9));
        }

        default TResult mist900() {
            return _helper().color(new OkLch(0.218, 0.008, 223.9));
        }

        default TResult mist950() {
            return _helper().color(new OkLch(0.148, 0.004, 228.8));
        }
    }

    public interface ColorTaupeApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult taupe50() {
            return _helper().color(new OkLch(0.986, 0.002, 67.8));
        }

        default TResult taupe100() {
            return _helper().color(new OkLch(0.96, 0.002, 17.2));
        }

        default TResult taupe200() {
            return _helper().color(new OkLch(0.922, 0.005, 34.3));
        }

        default TResult taupe300() {
            return _helper().color(new OkLch(0.868, 0.007, 39.5));
        }

        default TResult taupe400() {
            return _helper().color(new OkLch(0.714, 0.014, 41.2));
        }

        default TResult taupe500() {
            return _helper().color(new OkLch(0.547, 0.021, 43.1));
        }

        default TResult taupe600() {
            return _helper().color(new OkLch(0.438, 0.017, 39.3));
        }

        default TResult taupe700() {
            return _helper().color(new OkLch(0.367, 0.016, 35.7));
        }

        default TResult taupe800() {
            return _helper().color(new OkLch(0.268, 0.011, 36.5));
        }

        default TResult taupe900() {
            return _helper().color(new OkLch(0.214, 0.009, 43.1));
        }

        default TResult taupe950() {
            return _helper().color(new OkLch(0.147, 0.004, 49.3));
        }
    }

    public interface ColorBlackWhiteApi<TResult, Helper extends ColorHelper<TResult>>
            extends ApiHelperProvider<Helper> {
        default TResult black() {
            return _helper().color(new OkLch(0, 0, 0));
        }

        default TResult white() {
            return _helper().color(new OkLch(1, 0, 0));
        }
    }

    public interface Combined<TResult, Helper extends ColorHelper<TResult>>
            extends
            ColorBlackWhiteApi<TResult, Helper>,
            ColorRedApi<TResult, Helper>,
            ColorOrangeApi<TResult, Helper>,
            ColorAmberApi<TResult, Helper>,
            ColorYellowApi<TResult, Helper>,
            ColorLimeApi<TResult, Helper>,
            ColorGreenApi<TResult, Helper>,
            ColorEmeraldApi<TResult, Helper>,
            ColorTealApi<TResult, Helper>,
            ColorCyanApi<TResult, Helper>,
            ColorSkyApi<TResult, Helper>,
            ColorBlueApi<TResult, Helper>,
            ColorIndigoApi<TResult, Helper>,
            ColorVioletApi<TResult, Helper>,
            ColorPurpleApi<TResult, Helper>,
            ColorFuchsiaApi<TResult, Helper>,
            ColorPinkApi<TResult, Helper>,
            ColorRoseApi<TResult, Helper>,
            ColorSlateApi<TResult, Helper>,
            ColorGrayApi<TResult, Helper>,
            ColorZincApi<TResult, Helper>,
            ColorNeutralApi<TResult, Helper>,
            ColorStoneApi<TResult, Helper>,
            ColorMauveApi<TResult, Helper>,
            ColorOliveApi<TResult, Helper>,
            ColorMistApi<TResult, Helper>,
            ColorTaupeApi<TResult, Helper> {
    }

}
