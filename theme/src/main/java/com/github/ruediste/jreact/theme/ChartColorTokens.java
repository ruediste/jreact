package com.github.ruediste.jreact.theme;

import java.util.function.Consumer;

import com.github.ruediste.jreact.color.JColor;

/**
 * Color tokens for data visualization charts.
 */
public interface ChartColorTokens<Self> {
    /**
     * First categorical color in the chart palette.
     */
    Self chart1();

    /**
     * Second categorical color in the chart palette.
     */
    Self chart2();

    /**
     * Third categorical color in the chart palette.
     */
    Self chart3();

    /**
     * Fourth categorical color in the chart palette.
     */
    Self chart4();

    /**
     * Fifth categorical color in the chart palette.
     */
    Self chart5();

    public class Values {
        public JColor chart1;
        public JColor chart2;
        public JColor chart3;
        public JColor chart4;
        public JColor chart5;
    }

    public class Impl<Api> implements ChartColorTokens<Api> {

        private Values values;
        private Api api;
        private Consumer<JColor> apply;

        public Impl(Values values, Api api, Consumer<JColor> apply) {
            this.values = values;
            this.api = api;
            this.apply = apply;
        }

        @Override
        public Api chart1() {
            apply.accept(values.chart1);
            return api;
        }

        @Override
        public Api chart2() {
            apply.accept(values.chart2);
            return api;
        }

        @Override
        public Api chart3() {
            apply.accept(values.chart3);
            return api;
        }

        @Override
        public Api chart4() {
            apply.accept(values.chart4);
            return api;
        }

        @Override
        public Api chart5() {
            apply.accept(values.chart5);
            return api;
        }
    }
}
