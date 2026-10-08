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
public final class FirestoreProductDataSource_Factory implements Factory<FirestoreProductDataSource> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  public FirestoreProductDataSource_Factory(Provider<FirebaseFirestore> firestoreProvider) {
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public FirestoreProductDataSource get() {
    return newInstance(firestoreProvider.get());
  }

  public static FirestoreProductDataSource_Factory create(
      Provider<FirebaseFirestore> firestoreProvider) {
    return new FirestoreProductDataSource_Factory(firestoreProvider);
  }

  public static FirestoreProductDataSource newInstance(FirebaseFirestore firestore) {
    return new FirestoreProductDataSource(firestore);
  }
}
