package com.example.tomohiko_sato.owltube.di;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {
    PresentationComponent getPresentationComponent(PresentationModule module);
}
