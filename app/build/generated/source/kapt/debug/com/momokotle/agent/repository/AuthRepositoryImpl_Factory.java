package com.momokotle.agent.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.momokotle.agent.data.datastore.UserAccountDataStore;
import com.momokotle.agent.data.firestore.FirestoreUserDataSource;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<FirestoreUserDataSource> firestoreUserDataSourceProvider;

  private final Provider<UserAccountDataStore> userAccountDataStoreProvider;

  public AuthRepositoryImpl_Factory(Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirestoreUserDataSource> firestoreUserDataSourceProvider,
      Provider<UserAccountDataStore> userAccountDataStoreProvider) {
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.firestoreUserDataSourceProvider = firestoreUserDataSourceProvider;
    this.userAccountDataStoreProvider = userAccountDataStoreProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(firebaseAuthProvider.get(), firestoreUserDataSourceProvider.get(), userAccountDataStoreProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirestoreUserDataSource> firestoreUserDataSourceProvider,
      Provider<UserAccountDataStore> userAccountDataStoreProvider) {
    return new AuthRepositoryImpl_Factory(firebaseAuthProvider, firestoreUserDataSourceProvider, userAccountDataStoreProvider);
  }

  public static AuthRepositoryImpl newInstance(FirebaseAuth firebaseAuth,
      FirestoreUserDataSource firestoreUserDataSource, UserAccountDataStore userAccountDataStore) {
    return new AuthRepositoryImpl(firebaseAuth, firestoreUserDataSource, userAccountDataStore);
  }
}
