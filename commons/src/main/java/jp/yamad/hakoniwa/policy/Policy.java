package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.ActionModel;

public class Policy {
    private final HakoniwaTarget target;
    private final ActionModel<?> model;  // これ嫌だ。

    public Policy(HakoniwaTarget target, ActionModel<?> model) {
        this.target = target;
        this.model = model;
    }

    public HakoniwaTarget getTarget() {
        return this.target;
    }

    public ActionModel<?> getModel() {
        return this.model;
    }
}
