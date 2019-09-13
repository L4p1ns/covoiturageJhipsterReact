import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IChauffeur, defaultValue } from 'app/shared/model/chauffeur.model';

export const ACTION_TYPES = {
  SEARCH_CHAUFFEURS: 'chauffeur/SEARCH_CHAUFFEURS',
  FETCH_CHAUFFEUR_LIST: 'chauffeur/FETCH_CHAUFFEUR_LIST',
  FETCH_CHAUFFEUR: 'chauffeur/FETCH_CHAUFFEUR',
  CREATE_CHAUFFEUR: 'chauffeur/CREATE_CHAUFFEUR',
  UPDATE_CHAUFFEUR: 'chauffeur/UPDATE_CHAUFFEUR',
  DELETE_CHAUFFEUR: 'chauffeur/DELETE_CHAUFFEUR',
  RESET: 'chauffeur/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IChauffeur>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ChauffeurState = Readonly<typeof initialState>;

// Reducer

export default (state: ChauffeurState = initialState, action): ChauffeurState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CHAUFFEURS):
    case REQUEST(ACTION_TYPES.FETCH_CHAUFFEUR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHAUFFEUR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CHAUFFEUR):
    case REQUEST(ACTION_TYPES.UPDATE_CHAUFFEUR):
    case REQUEST(ACTION_TYPES.DELETE_CHAUFFEUR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CHAUFFEURS):
    case FAILURE(ACTION_TYPES.FETCH_CHAUFFEUR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHAUFFEUR):
    case FAILURE(ACTION_TYPES.CREATE_CHAUFFEUR):
    case FAILURE(ACTION_TYPES.UPDATE_CHAUFFEUR):
    case FAILURE(ACTION_TYPES.DELETE_CHAUFFEUR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CHAUFFEURS):
    case SUCCESS(ACTION_TYPES.FETCH_CHAUFFEUR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHAUFFEUR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHAUFFEUR):
    case SUCCESS(ACTION_TYPES.UPDATE_CHAUFFEUR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHAUFFEUR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/chauffeurs';
const apiSearchUrl = 'api/_search/chauffeurs';

// Actions

export const getSearchEntities: ICrudSearchAction<IChauffeur> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CHAUFFEURS,
  payload: axios.get<IChauffeur>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IChauffeur> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CHAUFFEUR_LIST,
  payload: axios.get<IChauffeur>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IChauffeur> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHAUFFEUR,
    payload: axios.get<IChauffeur>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IChauffeur> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHAUFFEUR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IChauffeur> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHAUFFEUR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IChauffeur> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHAUFFEUR,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
