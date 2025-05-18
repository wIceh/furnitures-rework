package me.wiceh.utils.command.help;

import java.util.Collections;
import java.util.List;

public interface Helpable {
    default List<HelpValue> getHelp() {
        return Collections.emptyList();
    }
}
