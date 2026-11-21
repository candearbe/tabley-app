package com.uadeapps.tabley.di

import android.content.Context
import androidx.room.Room
import com.uadeapps.tabley.data.local.TableyDatabase
import com.uadeapps.tabley.data.local.dao.CellDao
import com.uadeapps.tabley.data.local.dao.ColumnDao
import com.uadeapps.tabley.data.local.dao.NotebookDao
import com.uadeapps.tabley.data.local.dao.RowDao
import com.uadeapps.tabley.data.local.dao.TableDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TableyDatabase {
        return Room.databaseBuilder(
            context,
            TableyDatabase::class.java,
            "tabley_database"
        ).build()
    }

    @Provides
    fun provideNotebookDao(database: TableyDatabase): NotebookDao =
        database.notebookDao()

    @Provides
    fun provideTableDao(database: TableyDatabase): TableDao =
        database.tableDao()

    @Provides
    fun provideColumnDao(database: TableyDatabase): ColumnDao =
        database.columnDao()

    @Provides
    fun provideRowDao(database: TableyDatabase): RowDao =
        database.rowDao()

    @Provides
    fun provideCellDao(database: TableyDatabase): CellDao =
        database.cellDao()
}
