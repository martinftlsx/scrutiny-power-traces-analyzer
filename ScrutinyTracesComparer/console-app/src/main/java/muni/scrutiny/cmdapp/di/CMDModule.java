package muni.scrutiny.cmdapp.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import muni.scrutiny.cmdapp.actions.COTemplateFinderAction;
import muni.scrutiny.cmdapp.actions.CompareProfilesAction;
import muni.scrutiny.cmdapp.actions.ConcatTracesAction;
import muni.scrutiny.cmdapp.actions.CreateReferenceProfileAction;
import muni.scrutiny.cmdapp.actions.base.Action;

public class CMDModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Action.class)
                .annotatedWith(Names.named(CreateReferenceProfileAction.name))
                .toInstance(new CreateReferenceProfileAction());
        bind(Action.class)
                .annotatedWith(Names.named(CompareProfilesAction.name))
                .toInstance(new CompareProfilesAction());
        bind(Action.class)
                .annotatedWith(Names.named(COTemplateFinderAction.name))
                .toInstance(new COTemplateFinderAction());
        bind(Action.class)
                .annotatedWith(Names.named(ConcatTracesAction.name))
                .toInstance(new ConcatTracesAction());
    }
}
