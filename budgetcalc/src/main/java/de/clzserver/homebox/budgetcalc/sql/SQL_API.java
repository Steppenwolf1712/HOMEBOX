package de.clzserver.homebox.budgetcalc.sql;

import de.clzserver.homebox.budgetcalc.interfaces.Budget_API;
import de.clzserver.homebox.budgetcalc.interfaces.IBudget;
import de.clzserver.homebox.budgetcalc.interfaces.MonthEnum;
import de.clzserver.homebox.config.Config;

/**
 * Created by Marc Janßen on 26.08.2017.
 */
public class SQL_API implements Budget_API {
    @Override
    public boolean islocked() {
        return false;
    }

    @Override
    public String getCurrentUser() {
        Config cfg = Config.getInstance();

        return cfg.getValue(Config.USER_STRING_KEY);
    }

    @Override
    public IBudget[] getMonth(MonthEnum m_enum, int year) {
        return new IBudget[0];
    }

    @Override
    public void writeBudget(IBudget calc) {

    }
}
