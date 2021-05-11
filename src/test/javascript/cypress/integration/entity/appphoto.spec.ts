import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Appphoto e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/appphotos*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('appphoto');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Appphotos', () => {
    cy.intercept('GET', '/api/appphotos*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appphoto');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Appphoto').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Appphoto page', () => {
    cy.intercept('GET', '/api/appphotos*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appphoto');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('appphoto');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Appphoto page', () => {
    cy.intercept('GET', '/api/appphotos*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appphoto');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Appphoto');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Appphoto page', () => {
    cy.intercept('GET', '/api/appphotos*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appphoto');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Appphoto');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Appphoto', () => {
    cy.intercept('GET', '/api/appphotos*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appphoto');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Appphoto');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-10T22:17').invoke('val').should('equal', '2021-05-10T22:17');


    cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

    cy.setFieldSelectToLastOfEntity('appuser');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/appphotos*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appphoto');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Appphoto', () => {
    cy.intercept('GET', '/api/appphotos*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/appphotos/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appphoto');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('appphoto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/appphotos*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('appphoto');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
