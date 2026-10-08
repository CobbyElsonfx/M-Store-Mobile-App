package com.momokotle.agent.data.firestore;

import com.google.firebase.firestore.FirebaseFirestore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class FirestoreUserDataSource_Factory implements Factory<FirestoreUserDataSource> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  public FirestoreUserDataSource_Factory(Provider<FirebaseFirestore> firestoreProvider) {
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public FirestoreUserDataSource get() {
    return newInstance(firestoreProvider.get());
  }

  public static FirestoreUserDataSource_Factory create(
      Provider<FirebaseFirestore> firestoreProvider) {
    return new FirestoreUserDataSource_Factory(firestoreProvider);
  }

  public static FirestoreUserDataSource newInstance(FirebaseFirestore firestore) {
    return new FirestoreUserDataSource(firestore);
  }
}
